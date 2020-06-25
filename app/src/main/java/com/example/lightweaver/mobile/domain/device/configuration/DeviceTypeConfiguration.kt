package com.example.lightweaver.mobile.domain.device.configuration

sealed class DeviceTypeConfiguration() {
    class BasicDeviceConfiguration(): DeviceTypeConfiguration()
    class LightStripDeviceConfiguration(): DeviceTypeConfiguration()
    class TriPanelDeviceConfiguration(): DeviceTypeConfiguration()
}