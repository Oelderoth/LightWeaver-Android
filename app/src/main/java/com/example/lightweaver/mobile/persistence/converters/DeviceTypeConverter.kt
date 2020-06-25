package com.example.lightweaver.mobile.persistence.converters

import androidx.room.TypeConverter
import com.example.lightweaver.mobile.domain.device.DeviceType

class DeviceTypeConverter {
    @TypeConverter
    fun toString(enum: DeviceType): String = enum.name
    @TypeConverter
    fun toEnum(name: String): DeviceType = DeviceType.valueOf(name)
}