package com.example.lightweaver.mobile.domain.device.configuration

import com.example.lightweaver.mobile.domain.exception.InvalidProtocolException
import java.net.URL

sealed class ConnectionConfiguration() {
    data class HttpConfiguration(val url: URL, val networkName: String?, val discoverable: Boolean): ConnectionConfiguration() {
        init {
            if (url.protocol != "http") throw InvalidProtocolException("HttpConnection must be configured with an HTTP protocol")
        }
    }
}