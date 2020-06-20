package com.example.lightweaver.moblie.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.lightweaver.moblie.persistence.dao.HttpDeviceConfigurationDao
import com.example.lightweaver.moblie.persistence.entities.HttpDeviceConfiguration
import com.example.lightweaver.moblie.persistence.repository.DeviceConfigurationRepository

@Database(entities = [HttpDeviceConfiguration::class], version = 1)
abstract class LightWeaverDatabase: RoomDatabase() {
    protected abstract fun httpDeviceConfigurationDao(): HttpDeviceConfigurationDao

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