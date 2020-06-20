package com.example.lightweaver.moblie.ui.device

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.lightweaver.moblie.domain.device.DeviceConfiguration
import com.example.lightweaver.moblie.persistence.LightWeaverDatabase

class DeviceBrowserViewModel(application: Application) : AndroidViewModel(application) {
    val deviceConfigurations: LiveData<List<DeviceConfiguration>>

    init {
        val repository = LightWeaverDatabase.getInstance(application).deviceConfigurationRepository()
        deviceConfigurations = repository.getAll()
    }

//    private val _deviceDescriptorList = MutableLiveData<MutableList<DeviceDescriptor>>().apply {
//        value = mutableListOf()
//    }
//    val deviceDescriptorList: LiveData<MutableList<DeviceDescriptor>> = _deviceDescriptorList
//
//    fun addDeviceDescriptor(descriptor: DeviceDescriptor) {
//        _deviceDescriptorList.value?.add(descriptor)
//        _deviceDescriptorList.notifyObserver()
//    }

}