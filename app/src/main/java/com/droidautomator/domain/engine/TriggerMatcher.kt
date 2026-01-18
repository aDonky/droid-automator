package com.droidautomator.domain.engine

import com.droidautomator.domain.model.NotificationData
import com.droidautomator.domain.model.Trigger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TriggerMatcher @Inject constructor() {

    fun matches(trigger: Trigger, notificationData: NotificationData): Boolean {
        return when (trigger) {
            is Trigger.NotificationTrigger -> matchNotificationTrigger(trigger, notificationData)
        }
    }

    private fun matchNotificationTrigger(
        trigger: Trigger.NotificationTrigger,
        data: NotificationData
    ): Boolean {
        // Check package name
        if (trigger.packageName != data.packageName) {
            return false
        }

        // Check title filter (optional regex)
        trigger.titleFilter?.let { filter ->
            if (!matchesFilter(data.title, filter)) {
                return false
            }
        }

        // Check text filter (optional regex)
        trigger.textFilter?.let { filter ->
            if (!matchesFilter(data.text, filter)) {
                return false
            }
        }

        return true
    }

    private fun matchesFilter(text: String, filter: String): Boolean {
        return try {
            val regex = filter.toRegex(RegexOption.IGNORE_CASE)
            regex.containsMatchIn(text)
        } catch (e: Exception) {
            // If regex is invalid, fall back to simple contains check
            text.contains(filter, ignoreCase = true)
        }
    }
}
