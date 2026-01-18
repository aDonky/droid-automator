package com.droidautomator.ui.screens.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.droidautomator.data.repository.AutomationRepository
import com.droidautomator.domain.engine.VariableInfo
import com.droidautomator.domain.engine.VariableParser
import com.droidautomator.domain.model.Action
import com.droidautomator.domain.model.Automation
import com.droidautomator.domain.model.Trigger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EditorUiState(
    val isLoading: Boolean = true,
    val isEditing: Boolean = false,
    val automationId: Long? = null,
    val name: String = "",
    val enabled: Boolean = true,
    val packageName: String = "",
    val titleFilter: String = "",
    val textFilter: String = "",
    val urlTemplate: String = "",
    val isSaving: Boolean = false,
    val savedSuccessfully: Boolean = false,
    val availableVariables: List<VariableInfo> = emptyList()
)

@HiltViewModel
class EditorViewModel @Inject constructor(
    private val automationRepository: AutomationRepository,
    private val variableParser: VariableParser
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditorUiState())
    val uiState: StateFlow<EditorUiState> = _uiState.asStateFlow()

    fun loadAutomation(automationId: Long?) {
        viewModelScope.launch {
            _uiState.update { it.copy(
                isLoading = true,
                availableVariables = variableParser.getAvailableVariables()
            ) }

            if (automationId != null && automationId > 0) {
                val automation = automationRepository.getAutomationById(automationId)
                if (automation != null) {
                    val trigger = automation.trigger as? Trigger.NotificationTrigger
                    val urlAction = automation.actions.firstOrNull() as? Action.OpenUrlAction

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isEditing = true,
                            automationId = automationId,
                            name = automation.name,
                            enabled = automation.enabled,
                            packageName = trigger?.packageName ?: "",
                            titleFilter = trigger?.titleFilter ?: "",
                            textFilter = trigger?.textFilter ?: "",
                            urlTemplate = urlAction?.urlTemplate ?: ""
                        )
                    }
                    return@launch
                }
            }

            // New automation - set defaults
            _uiState.update {
                it.copy(
                    isLoading = false,
                    isEditing = false,
                    name = "Google Wallet Payment",
                    packageName = "com.google.android.apps.walletnfcrel",
                    urlTemplate = "keystone://spending?merchant={notification_title}&amount={parsed_amount}"
                )
            }
        }
    }

    fun updateName(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun updatePackageName(packageName: String) {
        _uiState.update { it.copy(packageName = packageName) }
    }

    fun updateTitleFilter(titleFilter: String) {
        _uiState.update { it.copy(titleFilter = titleFilter) }
    }

    fun updateTextFilter(textFilter: String) {
        _uiState.update { it.copy(textFilter = textFilter) }
    }

    fun updateUrlTemplate(urlTemplate: String) {
        _uiState.update { it.copy(urlTemplate = urlTemplate) }
    }

    fun insertVariable(variableName: String) {
        val currentUrl = _uiState.value.urlTemplate
        val variable = "{$variableName}"
        _uiState.update { it.copy(urlTemplate = currentUrl + variable) }
    }

    fun saveAutomation() {
        viewModelScope.launch {
            val state = _uiState.value

            if (state.name.isBlank() || state.packageName.isBlank() || state.urlTemplate.isBlank()) {
                return@launch
            }

            _uiState.update { it.copy(isSaving = true) }

            val trigger = Trigger.NotificationTrigger(
                packageName = state.packageName,
                titleFilter = state.titleFilter.takeIf { it.isNotBlank() },
                textFilter = state.textFilter.takeIf { it.isNotBlank() }
            )

            val action = Action.OpenUrlAction(urlTemplate = state.urlTemplate)

            val automation = Automation(
                id = state.automationId ?: 0,
                name = state.name,
                enabled = state.enabled,
                trigger = trigger,
                actions = listOf(action)
            )

            if (state.isEditing && state.automationId != null) {
                automationRepository.updateAutomation(automation)
            } else {
                automationRepository.saveAutomation(automation)
            }

            _uiState.update { it.copy(isSaving = false, savedSuccessfully = true) }
        }
    }

    fun deleteAutomation() {
        viewModelScope.launch {
            val automationId = _uiState.value.automationId ?: return@launch
            automationRepository.deleteAutomation(automationId)
            _uiState.update { it.copy(savedSuccessfully = true) }
        }
    }
}
