package com.example.lightweaver.moblie.ui.device

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DeviceBrowserViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Device Browser Fragment"
    }
    val text: LiveData<String> = _text
}