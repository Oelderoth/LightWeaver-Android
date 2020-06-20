package com.example.lightweaver.moblie.ui.device

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lightweaver.moblie.notifyObserver
import com.example.lightweaver.moblie.persistence.LightWeaverDatabase
import com.example.lightweaver.moblie.persistence.entities.DeviceConfiguration
import com.oelderoth.lightweaver.core.devices.DeviceDescriptor

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