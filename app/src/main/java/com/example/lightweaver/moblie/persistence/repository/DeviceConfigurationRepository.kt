package com.example.lightweaver.moblie.persistence.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lightweaver.moblie.domain.device.DeviceConfiguration
import com.example.lightweaver.moblie.domain.device.connection.HttpConnectionConfiguration
import com.example.lightweaver.moblie.domain.device.type.LightStripConfiguration
import com.example.lightweaver.moblie.domain.device.type.LightTriPanelConfiguration
import com.example.lightweaver.moblie.persistence.dao.DeviceConfigurationDao

class DeviceConfigurationRepository(private val deviceConfigurationDao: DeviceConfigurationDao) {
    suspend fun insert(deviceConfiguration: DeviceConfiguration) {
        throw NotImplementedError()
    }

    suspend fun get(uid: String): DeviceConfiguration? {
        val deviceConfiguration = deviceConfigurationDao.get(uid)
        return deviceConfiguration?.let { return DeviceConfiguration(it.uid, it.name, it.description, HttpConnectionConfiguration(), LightStripConfiguration()) }
    }

    fun getAll(): LiveData<List<DeviceConfiguration>> {
        //TODO: Remove temporary stub once insert/update functionality is in place to add real data
        return MutableLiveData<List<DeviceConfiguration>>().apply {
            value = listOf(DeviceConfiguration("0001", "Device 001", "First Test Device", HttpConnectionConfiguration(), LightStripConfiguration()),
                DeviceConfiguration("0002", "Device 002", "Second Test Device", HttpConnectionConfiguration(), LightStripConfiguration()),
                DeviceConfiguration("0003", "Device 003", "Third Test Device", HttpConnectionConfiguration(), LightTriPanelConfiguration()))
        }

//        return Transformations.map(deviceConfigurationDao.getAll()) { list ->
//            list.map { config -> DeviceConfiguration(config.uid, config.name, config.description, HttpConnectionConfiguration(), LightStripConfiguration()) }
//        }
    }

    suspend fun count(): Int {
        return deviceConfigurationDao.count()
    }
}