package com.droidautomator.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class Action {

    @Serializable
    @SerialName("open_url")
    data class OpenUrlAction(
        val urlTemplate: String
    ) : Action()

    // Future actions can be added here:
    // @Serializable
    // @SerialName("show_notification")
    // data class ShowNotificationAction(val title: String, val text: String) : Action()

    // @Serializable
    // @SerialName("http_request")
    // data class HttpRequestAction(val url: String, val method: String, val body: String?) : Action()

    // @Serializable
    // @SerialName("launch_app")
    // data class LaunchAppAction(val packageName: String) : Action()
}

fun Action.displayName(): String = when (this) {
    is Action.OpenUrlAction -> "Open URL"
}
