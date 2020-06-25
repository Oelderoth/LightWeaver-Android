package com.example.lightweaver.mobile.domain.device

import com.example.lightweaver.mobile.domain.device.configuration.ConnectionConfiguration

enum class ConnectionType {
    HTTP;

    companion object {
        fun fromConfiguration(config: ConnectionConfiguration): ConnectionType {
            return when (config) {
                is ConnectionConfiguration.HttpConfiguration -> HTTP
            }
        }
    }
}