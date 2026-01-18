package com.droidautomator.domain.model

data class Automation(
    val id: Long = 0,
    val name: String,
    val enabled: Boolean = true,
    val trigger: Trigger,
    val actions: List<Action>,
    val createdAt: Long = System.currentTimeMillis()
)
