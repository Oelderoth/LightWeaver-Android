package com.example.lightweaver.moblie.persistence.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.lightweaver.moblie.persistence.dao.HttpDeviceConfigurationDao
import com.example.lightweaver.moblie.persistence.entities.DeviceConfiguration
import com.example.lightweaver.moblie.persistence.entities.HttpDeviceConfiguration
import java.lang.RuntimeException

class DeviceConfigurationRepository(private val httpDeviceConfigurationDao: HttpDeviceConfigurationDao) {
    suspend fun insert(deviceConfiguration: DeviceConfiguration) {
        when (deviceConfiguration) {
            is HttpDeviceConfiguration -> httpDeviceConfigurationDao.insert(deviceConfiguration)
            else -> throw RuntimeException("${deviceConfiguration::class::simpleName!!} is not a supported ${DeviceConfiguration::class::simpleName!!}")
        }
    }


    suspend fun get(uid: String): DeviceConfiguration? {
        val httpDevice = httpDeviceConfigurationDao.get(uid)
        if (httpDevice != null) return httpDevice

        // TODO: Once other device types are supported, search for those here

        return null
    }

    fun getAll(): LiveData<List<DeviceConfiguration>> {
        val httpDeviceConfigurations = httpDeviceConfigurationDao.getAll()
        // TODO: Once other device types are supported, zip them together here rather than casting
        return Transformations.map(httpDeviceConfigurations) { value -> value as List<DeviceConfiguration>}
    }

    suspend fun count(): Int {

        return httpDeviceConfigurationDao.count()
    }
}