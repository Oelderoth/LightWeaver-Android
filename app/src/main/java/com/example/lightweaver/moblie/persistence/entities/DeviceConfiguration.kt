package com.example.lightweaver.moblie.persistence.entities

import androidx.room.Ignore

// DeviceConfiguration acts as a parent class to enforce that all types of device configurations
// contain the required fields. It is not directly persisted, and so has no @Entity annotation.
// Since all fields are open and overridden by subclasses, they are marked as @Ignore to prevent duplicate columns
abstract class DeviceConfiguration(
    @Ignore open val uid: String,
    @Ignore open val name: String,
    @Ignore open val description: String?)