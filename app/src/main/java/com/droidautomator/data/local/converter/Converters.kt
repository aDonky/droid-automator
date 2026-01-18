package com.droidautomator.data.local.converter

import com.droidautomator.domain.model.Action
import com.droidautomator.domain.model.Trigger
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object Converters {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    fun triggerToJson(trigger: Trigger): String {
        return json.encodeToString(trigger)
    }

    fun jsonToTrigger(jsonString: String): Trigger {
        return json.decodeFromString(jsonString)
    }

    fun actionsToJson(actions: List<Action>): String {
        return json.encodeToString(actions)
    }

    fun jsonToActions(jsonString: String): List<Action> {
        return json.decodeFromString(jsonString)
    }
}
