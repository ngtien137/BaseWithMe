package com.lhd.view.basewithme.ui

import androidx.recyclerview.widget.GridLayoutManager
import com.base.baselibrary.utils.observer
import com.base.baselibrary.viewmodel.autoViewModels
import com.lhd.view.basewithme.R
import com.lhd.view.basewithme.adapter.media.IMediaListener
import com.lhd.view.basewithme.adapter.media.MediaAdapter
import com.lhd.view.basewithme.databinding.FragmentMediaBinding
import com.lhd.view.basewithme.viewmodel.MediaViewModel

class MediaFragment : BaseNavFragment<FragmentMediaBinding>(), IMediaListener {
    override fun getLayoutId(): Int {
        return R.layout.fragment_media
    }

    private val viewModel by autoViewModels<MediaViewModel>()

    private val adapter by lazy {
        MediaAdapter().apply {
            listener = this@MediaFragment
        }
    }

    override fun initBinding() {
        binding.viewModel = viewModel
        binding.adapter = adapter
    }

    override fun initView() {
        viewModel.loadListMedia()
        observer(viewModel.liveListPhoto) {
            adapter.data = it
        }
    }

    var mediaCountRow = 2
    override fun onMediaClick() {
        mediaCountRow++
        binding.rvList.layoutManager = GridLayoutManager(activity, mediaCountRow)
        adapter.mediaRowCount = mediaCountRow
    }

}