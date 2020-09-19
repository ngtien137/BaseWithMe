package com.lhd.view.basewithme.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.base.baselibrary.viewmodel.Auto
import com.base.baselibrary.viewmodel.Event
import com.lhd.view.basewithme.repository.MediaRepository

class MediaViewModel @Auto private constructor(private val mediaRepository: MediaRepository) :
    ViewModel() {
    val liveListPhoto by lazy {
        mediaRepository.liveListPhoto
    }
    val eventLoading = MutableLiveData(Event())
    fun loadListMedia(
        forceLoad: Boolean = false,
        eventLoading: MutableLiveData<Event>? = this.eventLoading
    ) {
        mediaRepository.loadListPhoto(forceLoad, eventLoading)
    }
}