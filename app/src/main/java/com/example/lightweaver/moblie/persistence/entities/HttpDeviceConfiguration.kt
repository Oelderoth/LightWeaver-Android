package com.example.lightweaver.moblie.persistence.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "http_device_configuration")
data class HttpDeviceConfiguration(
    @PrimaryKey override val uid: String,
    override val name: String,
    override val description: String?,
    val ipAddress: String
) : DeviceConfiguration(uid, name, description)