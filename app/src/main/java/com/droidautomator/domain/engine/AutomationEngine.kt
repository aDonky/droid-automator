package com.droidautomator.domain.engine

import android.util.Log
import com.droidautomator.data.repository.AutomationRepository
import com.droidautomator.data.repository.LogRepository
import com.droidautomator.domain.model.Automation
import com.droidautomator.domain.model.NotificationData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AutomationEngine @Inject constructor(
    private val automationRepository: AutomationRepository,
    private val logRepository: LogRepository,
    private val triggerMatcher: TriggerMatcher,
    private val actionExecutor: ActionExecutor,
    private val variableParser: VariableParser
) {

    companion object {
        private const val TAG = "AutomationEngine"
    }

    private val json = Json { prettyPrint = true }

    suspend fun processNotification(notificationData: NotificationData) = withContext(Dispatchers.IO) {
        Log.d(TAG, "Processing notification from ${notificationData.packageName}")

        val enabledAutomations = automationRepository.getEnabledAutomationsSync()

        for (automation in enabledAutomations) {
            if (triggerMatcher.matches(automation.trigger, notificationData)) {
                Log.d(TAG, "Automation '${automation.name}' matched")
                executeAutomation(automation, notificationData)
            }
        }
    }

    private suspend fun executeAutomation(automation: Automation, notificationData: NotificationData) {
        val variables = variableParser.buildContext(notificationData)
        val triggerDataJson = json.encodeToString(
            mapOf(
                "packageName" to notificationData.packageName,
                "title" to notificationData.title,
                "text" to notificationData.text,
                "variables" to variables
            )
        )

        var allSuccess = true
        val messages = mutableListOf<String>()

        for (action in automation.actions) {
            val result = actionExecutor.execute(action, variables)

            when (result) {
                is ActionResult.Success -> {
                    messages.add(result.message)
                }
                is ActionResult.Failure -> {
                    allSuccess = false
                    messages.add(result.error)
                }
            }
        }

        logRepository.logExecution(
            automationId = automation.id,
            automationName = automation.name,
            triggerData = triggerDataJson,
            success = allSuccess,
            message = messages.joinToString("; ")
        )

        Log.d(TAG, "Automation '${automation.name}' completed. Success: $allSuccess")
    }
}
