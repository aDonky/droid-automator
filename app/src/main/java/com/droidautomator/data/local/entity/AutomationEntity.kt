package com.droidautomator.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "automations")
data class AutomationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val enabled: Boolean = true,
    val triggerJson: String,
    val actionsJson: String,
    val createdAt: Long = System.currentTimeMillis()
)
