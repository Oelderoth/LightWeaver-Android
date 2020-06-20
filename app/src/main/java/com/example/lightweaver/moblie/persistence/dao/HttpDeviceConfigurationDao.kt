package com.example.lightweaver.moblie.persistence.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.lightweaver.moblie.persistence.entities.DeviceConfiguration
import com.example.lightweaver.moblie.persistence.entities.HttpDeviceConfiguration
import java.lang.RuntimeException

@Dao
abstract class HttpDeviceConfigurationDao {
    @Query("SELECT * FROM http_device_configuration")
    abstract fun getAll(): LiveData<List<HttpDeviceConfiguration>>

    @Query("SELECT * FROM http_device_configuration WHERE uid == :uid LIMIT 1")
    abstract suspend fun get(uid: String): HttpDeviceConfiguration?

    @Query("SELECT count(*) FROM http_device_configuration")
    abstract suspend fun count(): Int

    @Insert
    abstract suspend fun insert(deviceConfiguration: HttpDeviceConfiguration)

    @Update
    abstract suspend fun update(deviceConfiguration: HttpDeviceConfiguration)
}