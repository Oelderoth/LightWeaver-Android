package com.example.lightweaver.moblie.persistence.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "device_configuration")
class DeviceConfiguration(
    @PrimaryKey val uid: String,
    val name: String,
    val description: String?,
    val connectionType: DeviceConnectionType,
    val deviceType: DeviceType)