package com.base.baselibrary.utils.player

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.SeekBar
import androidx.core.content.FileProvider
import androidx.lifecycle.*
import com.base.baselibrary.utils.error.BaseLibraryException
import com.base.baselibrary.utils.getApplication
import com.base.baselibrary.views.ext.loge
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import java.io.File

class AppPlayer : LifecycleObserver {

    companion object {
        var URI_AUTHORITY = ""
    }

    var isReversedRanger = false
    var playerView: PlayerView? = null
    var duration = 0L
    var currentProgress = 0L
    private var media: SimpleExoPlayer? = null
    var liveState = MutableLiveData(State.NOT_READY)
    var listener: IAppPlayerListener? = null
    var isIniting = false

    var minCut = 0L
        private set
    var maxCut = 0L
        private set
    var repeatMode = SimpleExoPlayer.REPEAT_MODE_ALL
        private set

    private var thread: Thread? = null
    private var runnable = Runnable {
        media?.let {
            try {
                while (thread != null && !thread!!.isInterrupted) {
                    if (liveState.value == State.PLAYING) {
                        handler.sendEmptyMessage(0)
                        Thread.sleep(100)
                    }
                }
            } catch (e: Exception) {
                loge("Error: $e")
            }

        }
    }
    private var handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            media?.let {
                var progress = it.currentPosition
                var fixProgress = false
                var isEnd = false
                if (minCut != 0L || maxCut != duration) {
                    if (isReversedRanger) {
                        if (progress in minCut..maxCut) {
                            progress = if (maxCut >= duration)
                                0
                            else maxCut + 1
                            fixProgress = true
                        }
                    } else {
                        if (progress < minCut) {
                            progress = minCut
                            fixProgress = true
                        } else if (progress > maxCut) {
                            if (media?.repeatMode == SimpleExoPlayer.REPEAT_MODE_ALL) {
                                //loge("CutMode: Reset to min cut")
                                progress = minCut
                                fixProgress = true
                            } else {
                                //loge("CutMode: Stop at max cut")
                                progress = maxCut
                                fixProgress = true
                                isEnd = true
                            }
                        }
                        //loge("Progress: $progress, Fixed: $fixProgress, Range[$minCut,$maxCut]")
                    }
                }
                if (fixProgress)
                    seek(progress, true)
                currentProgress = progress
                listener?.onProgressChange(currentProgress)
                if (isEnd) {
                    stop()
                    listener?.onVideoEnd()
                }
            }
        }
    }

    fun init(path: String) {
        if (URI_AUTHORITY.isEmpty()) {
            throw BaseLibraryException("Please configure uri authority by AppPlayer.URI_AUTHORITY equals your authority at tag provider in manifest")
        }
        liveState.value = State.NOT_READY
        isIniting = true
        listener?.onLoadStart()
        currentProgress = 0
        val uri = FileProvider.getUriForFile(
            getApplication(),
            URI_AUTHORITY,
            File(path)
        )
        media = SimpleExoPlayer.Builder(getApplication(), DefaultRenderersFactory(getApplication()))
            .build()//,trackSelector,loadControl)
        val dataSourceFactory = DefaultDataSourceFactory(
            getApplication(),
            Util.getUserAgent(
                getApplication(),
                "AppPlayer"
            )
        )
        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri)

        media?.volume = 0f
        media?.addListener(object : Player.EventListener {

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                super.onPlayerStateChanged(playWhenReady, playbackState)
                if (playbackState == ExoPlayer.STATE_READY) {
                    if (media != null && isIniting) {
                        media?.volume = 1f
                        isIniting = false
                        if (liveState.value == State.NOT_READY) {
                            liveState.value = State.PLAYING
                            val duration = media?.duration ?: 0
                            this@AppPlayer.duration = duration
                            if (maxCut == 0L)
                                maxCut = duration
                            listener?.onLoadComplete()
                        }
                    }
                } else if (playbackState == ExoPlayer.STATE_ENDED) {
                    stop()
                    liveState.value = State.PAUSE
                    listener?.onVideoEnd()
                }
            }

            override fun onPlayerError(error: ExoPlaybackException) {
                media = null
                loge("Player error: $error")
                listener?.onPlayerError()
            }
        })
        media?.repeatMode = repeatMode
        playerView?.player = media
        media?.prepare(mediaSource)
        media?.playWhenReady = true
    }

    private fun clearThread() {
        thread?.interrupt()
        thread = null
    }

    fun play() {
        clearThread()
        liveState.value = State.PLAYING
        media?.playWhenReady = true
        thread = Thread(runnable)
        thread?.start()
    }

    fun stop() {
        media?.let {
            seekToMin(false)
            liveState.value = State.STOP
            it.playWhenReady = false
            //media?.stop()
        }
        clearThread()
    }

    fun release() {
        maxCut = 0
        minCut = 0
        duration = 0L
        stop()
        clearThread()
        if (liveState.value == State.PLAYING || liveState.value == State.PAUSE)
            liveState.value = State.STOP
        media?.let {
            it.stop()
            it.release()
            media = null
        }
    }

    fun pause() {
        media?.let {
            liveState.value = State.PAUSE
            currentProgress = it.currentPosition
            media?.playWhenReady = false
        }
        clearThread()
    }

    fun seek(progress: Long, isPlaying: Boolean = true) {
        //loge("Seek to $progress")
        currentProgress = progress
        media?.seekTo(progress)
        media?.playWhenReady = isPlaying
        if (isPlaying) {
            liveState.value = State.PLAYING
        } else {
            if (liveState.value == State.PLAYING) {
                liveState.value = State.PAUSE
            }
        }

    }

    fun seekToMin(isPlaying: Boolean = true) {
        if (isReversedRanger) {
            seek(0, isPlaying)
        } else {
            seek(minCut, isPlaying)
        }
    }

    fun setRange(minValue: Long, maxValue: Long, reversedRanger: Boolean = false) {
        isReversedRanger = reversedRanger
        this.minCut = minValue
        if (maxValue == -1L)
            this.maxCut = duration
        else
            this.maxCut = maxValue
        //loge("Range:[$minCut,$maxCut], Duration: $duration")
    }

    fun isPlaying() = liveState.value == State.PLAYING
    fun changeRepeat(): Int {
        var mode = SimpleExoPlayer.REPEAT_MODE_OFF
        if (media != null) {
            mode = if (media?.repeatMode == SimpleExoPlayer.REPEAT_MODE_ALL) {
                SimpleExoPlayer.REPEAT_MODE_OFF
            } else {
                SimpleExoPlayer.REPEAT_MODE_ALL
            }
        }
        media?.repeatMode = mode
        return mode
    }

    fun setRepeatMode(repeatMode: Int) {
        this.repeatMode = repeatMode
        media?.repeatMode = repeatMode
    }

    private var pauseByTouch = false
    fun attachWithSeekBar(
        sb: SeekBar?,
        onStartTouch: () -> Unit = {},
        onResumeWhenPauseByTouch: () -> Unit = {},
        onProgressChange: (fromUser: Boolean) -> Unit = {}
    ) {
        sb?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (seekBar?.isPressed == true) {
                    seek(progress.toLong(), false)
                    listener?.onProgressChange(progress.toLong())
                }
                onProgressChange(fromUser)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                onStartTouch()
                if (isPlaying()) {
                    pauseByTouch = true
                    pause()
                }
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (pauseByTouch) {
                    onResumeWhenPauseByTouch()
                    pauseByTouch = false
                    seek(currentProgress, true)
                }
            }

        })
    }


    private var viewLifecycleOwner: LifecycleOwner? = null
    fun bindToLifecycle(viewLifecycleOwner: LifecycleOwner) {
        viewLifecycleOwner.lifecycle.removeObserver(this)
        this.viewLifecycleOwner = viewLifecycleOwner
        viewLifecycleOwner.lifecycle.addObserver(this)
    }

    private var isPauseByLifecycle = false

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private fun onLifeCyclePause() {
        if (isPlaying()) {
            isPauseByLifecycle = true
            pause()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun onLifeCycleResume() {
        if (isPauseByLifecycle) {
            play()
            isPauseByLifecycle = false
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun onLifeCycleDestroy() {
        release()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun onLifeCycleStop() {

    }

    enum class State {
        READY, PLAYING, STOP, PAUSE, NOT_READY
    }

    interface IAppPlayerListener {
        fun onLoadComplete() {}
        fun onLoadStart() {}
        fun onVideoEnd() {}
        fun onProgressChange(progress: Long) {}
        fun onPlayerError() {}
    }
}