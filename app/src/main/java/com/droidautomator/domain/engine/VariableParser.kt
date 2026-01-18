package com.droidautomator.domain.engine

import com.droidautomator.domain.model.NotificationData
import java.net.URLEncoder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VariableParser @Inject constructor() {

    companion object {
        private val AMOUNT_REGEX = """[£$€¥]?\s*([\d,]+\.?\d*)""".toRegex()
        private val VARIABLE_REGEX = """\{([^}]+)\}""".toRegex()
    }

    fun parseTemplate(template: String, context: Map<String, String>): String {
        return VARIABLE_REGEX.replace(template) { matchResult ->
            val variableName = matchResult.groupValues[1]
            val value = context[variableName] ?: matchResult.value
            URLEncoder.encode(value, "UTF-8")
        }
    }

    fun buildContext(notificationData: NotificationData): Map<String, String> {
        val context = mutableMapOf(
            "notification_title" to notificationData.title,
            "notification_text" to notificationData.text,
            "notification_package" to notificationData.packageName,
            "timestamp" to notificationData.timestamp.toString()
        )

        // Extract amount from text
        extractAmount(notificationData.text)?.let { amount ->
            context["parsed_amount"] = amount
        }

        return context
    }

    fun extractAmount(text: String): String? {
        return AMOUNT_REGEX.find(text)?.groupValues?.getOrNull(1)?.takeIf { it.isNotEmpty() }
    }

    fun getAvailableVariables(): List<VariableInfo> {
        return listOf(
            VariableInfo(
                name = "notification_title",
                description = "The notification title",
                example = "GREGGS"
            ),
            VariableInfo(
                name = "notification_text",
                description = "The notification body text",
                example = "£1.35 with Mastercard ••2722"
            ),
            VariableInfo(
                name = "notification_package",
                description = "The source app package name",
                example = "com.google.android.apps.walletnfcrel"
            ),
            VariableInfo(
                name = "parsed_amount",
                description = "Numeric amount extracted from text",
                example = "1.35"
            ),
            VariableInfo(
                name = "timestamp",
                description = "Unix timestamp of the notification",
                example = "1705612800000"
            )
        )
    }
}

data class VariableInfo(
    val name: String,
    val description: String,
    val example: String
)
