package com.droidautomator.ui.screens.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import com.droidautomator.service.DroidNotificationListenerService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class SettingsUiState(
    val isNotificationListenerEnabled: Boolean = false
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    fun refreshPermissionStatus() {
        val isEnabled = DroidNotificationListenerService.isNotificationListenerEnabled(context)
        _uiState.update { it.copy(isNotificationListenerEnabled = isEnabled) }
    }
}
