package com.example.lightweaver.moblie.persistence.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.lightweaver.moblie.persistence.entities.DeviceConfiguration

@Dao
abstract class DeviceConfigurationDao {
    @Query("SELECT * FROM device_configuration")
    abstract fun getAll(): LiveData<List<DeviceConfiguration>>

    @Query("SELECT * FROM device_configuration WHERE uid == :uid LIMIT 1")
    abstract suspend fun get(uid: String): DeviceConfiguration?

    @Query("SELECT count(*) FROM device_configuration")
    abstract suspend fun count(): Int

    @Insert
    abstract suspend fun insert(deviceConfiguration: DeviceConfiguration)

    @Update
    abstract suspend fun update(deviceConfiguration: DeviceConfiguration)
}