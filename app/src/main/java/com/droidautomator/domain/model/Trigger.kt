package com.droidautomator.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class Trigger {

    @Serializable
    @SerialName("notification")
    data class NotificationTrigger(
        val packageName: String,
        val titleFilter: String? = null,
        val textFilter: String? = null
    ) : Trigger()

    // Future triggers can be added here:
    // @Serializable
    // @SerialName("time")
    // data class TimeTrigger(val hour: Int, val minute: Int, val daysOfWeek: List<Int>) : Trigger()

    // @Serializable
    // @SerialName("battery")
    // data class BatteryTrigger(val level: Int, val isCharging: Boolean?) : Trigger()

    // @Serializable
    // @SerialName("wifi")
    // data class WifiTrigger(val ssid: String?, val connected: Boolean) : Trigger()
}

fun Trigger.displayName(): String = when (this) {
    is Trigger.NotificationTrigger -> "Notification from ${packageName.substringAfterLast('.')}"
}
