package com.example.lightweaver.mobile.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.lightweaver.mobile.persistence.dao.DeviceAndTypeConfigurationDao
import com.example.lightweaver.mobile.persistence.dao.DeviceConfigurationDao
import com.example.lightweaver.mobile.persistence.converters.DeviceTypeConverter
import com.example.lightweaver.mobile.persistence.entities.*
import com.example.lightweaver.mobile.persistence.repository.DeviceConfigurationRepository

@Database(entities = [
    DeviceConfigurationEntity.HttpDeviceConfiguration::class,
    DeviceTypeConfigurationEntity.BasicDeviceConfiguration::class,
    DeviceTypeConfigurationEntity.LightStripDeviceConfiguration::class,
    DeviceTypeConfigurationEntity.TriPanelDeviceConfiguration::class
], version = 1)
@TypeConverters(DeviceTypeConverter::class)
abstract class LightWeaverDatabase: RoomDatabase() {
    protected abstract fun httpDeviceConfigurationDao(): DeviceConfigurationDao
    protected abstract fun httpDeviceAndTypeConfigurationDao(): DeviceAndTypeConfigurationDao

    fun deviceConfigurationRepository(): DeviceConfigurationRepository {
        return DeviceConfigurationRepository(httpDeviceConfigurationDao(), httpDeviceAndTypeConfigurationDao())
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