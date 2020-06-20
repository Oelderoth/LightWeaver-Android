package com.example.lightweaver.moblie.persistence.entities

import androidx.room.TypeConverter

enum class DeviceType {
    LIGHTSTRIP,
    TRIPANEL;

    companion object {
        class Converter {
            @TypeConverter
            fun toString(enum: DeviceType): String = enum.name
            @TypeConverter
            fun toEnum(name: String): DeviceType = valueOf(name)
        }
    }
}