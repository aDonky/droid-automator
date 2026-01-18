package com.droidautomator.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "logs")
data class LogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val automationId: Long,
    val automationName: String,
    val timestamp: Long,
    val triggerData: String,
    val success: Boolean,
    val message: String
)
