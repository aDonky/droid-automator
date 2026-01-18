package com.droidautomator.domain.engine

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.droidautomator.domain.model.Action
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActionExecutor @Inject constructor(
    @ApplicationContext private val context: Context,
    private val variableParser: VariableParser
) {

    companion object {
        private const val TAG = "ActionExecutor"
    }

    suspend fun execute(action: Action, variables: Map<String, String>): ActionResult {
        return when (action) {
            is Action.OpenUrlAction -> executeOpenUrl(action, variables)
        }
    }

    private fun executeOpenUrl(action: Action.OpenUrlAction, variables: Map<String, String>): ActionResult {
        return try {
            val url = variableParser.parseTemplate(action.urlTemplate, variables)
            Log.d(TAG, "Opening URL: $url")

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            context.startActivity(intent)

            ActionResult.Success("Opened URL: $url")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to open URL", e)
            ActionResult.Failure("Failed to open URL: ${e.message}")
        }
    }
}

sealed class ActionResult {
    data class Success(val message: String) : ActionResult()
    data class Failure(val error: String) : ActionResult()

    val isSuccess: Boolean get() = this is Success
}
