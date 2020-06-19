package com.example.lightweaver.moblie.ui.devicegroup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DeviceGroupBrowserViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is DeviceGroup Browser Fragment"
    }
    val text: LiveData<String> = _text
}