package com.example.lightweaver.moblie.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.lightweaver.moblie.persistence.dao.DeviceConfigurationDao
import com.example.lightweaver.moblie.persistence.entities.DeviceConfiguration
import com.example.lightweaver.moblie.persistence.entities.DeviceConnectionType
import com.example.lightweaver.moblie.persistence.entities.DeviceType
import com.example.lightweaver.moblie.persistence.repository.DeviceConfigurationRepository

@Database(entities = [DeviceConfiguration::class], version = 1)
@TypeConverters(DeviceConnectionType.Companion.Converter::class, DeviceType.Companion.Converter::class)
abstract class LightWeaverDatabase: RoomDatabase() {
    protected abstract fun httpDeviceConfigurationDao(): DeviceConfigurationDao

    fun deviceConfigurationRepository(): DeviceConfigurationRepository {
        return DeviceConfigurationRepository(httpDeviceConfigurationDao())
    }

    companion object {
        @Volatile
        private var INSTANCE: LightWeaverDatabase? = null

        fun getInstance(context: Context): LightWeaverDatabase {
            val currentInstance = INSTANCE
            if (currentInstance  != null) {
                return currentInstance
            }
            synchronized(this) {
                val newInstance = Room.databaseBuilder(context.applicationContext, LightWeaverDatabase::class.java, "lightweaver_database").build()
                INSTANCE = newInstance
                return newInstance
            }
        }
    }
}