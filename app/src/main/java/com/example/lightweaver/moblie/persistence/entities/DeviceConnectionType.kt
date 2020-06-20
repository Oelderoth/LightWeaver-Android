package com.example.lightweaver.moblie.persistence.entities

import androidx.room.TypeConverter

enum class DeviceConnectionType {
    HTTP,
    GRPC,
    BLUETOOTH;

    companion object {
        class Converter {
            @TypeConverter
            fun toString(enum: DeviceConnectionType): String = enum.name
            @TypeConverter
            fun toEnum(name: String): DeviceConnectionType = valueOf(name)
        }
    }
}