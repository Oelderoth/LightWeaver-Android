package com.example.lightweaver.moblie.persistence.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.room.*
import com.example.lightweaver.moblie.persistence.entities.DeviceConfiguration

@Dao
abstract class DeviceConfigurationDao {
    @Query("SELECT * FROM http_device_configuration")
    protected abstract fun getAllHttpDevices(): LiveData<List<DeviceConfiguration.HttpDeviceConfiguration>>

    @Query("SELECT * FROM http_device_configuration WHERE uid == :uid LIMIT 1")
    protected abstract suspend fun getHttpDevice(uid: String): DeviceConfiguration.HttpDeviceConfiguration?

    @Update
    protected abstract suspend fun updateHttpDevice(deviceConfiguration: DeviceConfiguration.HttpDeviceConfiguration)

    @Query("SELECT count(*) FROM http_device_configuration")
    abstract suspend fun count(): Int

    fun getAllDevices(): LiveData<List<DeviceConfiguration>> {
        return MediatorLiveData<List<DeviceConfiguration>>().apply {
            addSource(getAllHttpDevices()) { v -> value = v}
        }
    }

    suspend fun getDevice(uid: String): DeviceConfiguration? {
        return getHttpDevice(uid)
    }
}