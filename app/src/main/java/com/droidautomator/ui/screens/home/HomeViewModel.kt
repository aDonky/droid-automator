package com.droidautomator.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.droidautomator.data.repository.AutomationRepository
import com.droidautomator.domain.model.Automation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val automations: List<Automation> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val automationRepository: AutomationRepository
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> = automationRepository.getAllAutomations()
        .map { automations ->
            HomeUiState(
                automations = automations,
                isLoading = false
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeUiState()
        )

    fun toggleAutomation(automationId: Long, enabled: Boolean) {
        viewModelScope.launch {
            automationRepository.setEnabled(automationId, enabled)
        }
    }

    fun deleteAutomation(automationId: Long) {
        viewModelScope.launch {
            automationRepository.deleteAutomation(automationId)
        }
    }
}
