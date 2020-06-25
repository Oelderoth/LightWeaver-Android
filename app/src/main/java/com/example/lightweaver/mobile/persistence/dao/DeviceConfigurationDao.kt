package com.example.lightweaver.mobile.persistence.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.example.lightweaver.mobile.persistence.entities.DeviceConfigurationEntity

@Dao
abstract class DeviceConfigurationDao {
    @Query("SELECT * FROM http_device_configuration")
    protected abstract fun getAllHttpDevices(): LiveData<List<DeviceConfigurationEntity.HttpDeviceConfiguration>>

    @Query("SELECT * FROM http_device_configuration WHERE uid == :uid LIMIT 1")
    protected abstract suspend fun getHttpDevice(uid: String): DeviceConfigurationEntity.HttpDeviceConfiguration?

    @Update
    protected abstract suspend fun updateHttpDevice(deviceConfiguration: DeviceConfigurationEntity.HttpDeviceConfiguration)

    @Query("SELECT count(*) FROM http_device_configuration")
    abstract suspend fun count(): Int

    fun getAllDevices(): LiveData<List<DeviceConfigurationEntity>> {
        return MediatorLiveData<List<DeviceConfigurationEntity>>().apply {
            addSource(getAllHttpDevices()) { v -> value = v}
        }
    }

    suspend fun getDevice(uid: String): DeviceConfigurationEntity? {
        return getHttpDevice(uid)
    }
}