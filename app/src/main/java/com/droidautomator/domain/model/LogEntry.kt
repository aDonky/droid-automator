package com.droidautomator.domain.model

data class LogEntry(
    val id: Long = 0,
    val automationId: Long,
    val automationName: String,
    val timestamp: Long,
    val triggerData: String,
    val success: Boolean,
    val message: String
)
