package com.example.lightweaver.moblie.ui.device

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lightweaver.moblie.notifyObserver
import com.oelderoth.lightweaver.core.devices.DeviceDescriptor
import com.oelderoth.lightweaver.core.devices.SupportedFeature
import com.oelderoth.lightweaver.http.devices.HttpDeviceDescriptor

class DeviceBrowserViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Device Browser Fragment"
    }
    val text: LiveData<String> = _text

    private val _deviceDescriptorList = MutableLiveData<MutableList<DeviceDescriptor>>().apply {
        value = mutableListOf()
    }
    val deviceDescriptorList: LiveData<MutableList<DeviceDescriptor>> = _deviceDescriptorList

    fun addDeviceDescriptor(descriptor: DeviceDescriptor) {
        _deviceDescriptorList.value?.add(descriptor)
        Log.i("DEV", "Added descriptor ${descriptor.name}")
        _deviceDescriptorList.notifyObserver()
    }
}