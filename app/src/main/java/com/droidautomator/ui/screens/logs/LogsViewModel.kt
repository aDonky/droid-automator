package com.droidautomator.ui.screens.logs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.droidautomator.data.repository.LogRepository
import com.droidautomator.domain.model.LogEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LogsUiState(
    val logs: List<LogEntry> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class LogsViewModel @Inject constructor(
    private val logRepository: LogRepository
) : ViewModel() {

    val uiState: StateFlow<LogsUiState> = logRepository.getRecentLogs(100)
        .map { logs ->
            LogsUiState(
                logs = logs,
                isLoading = false
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = LogsUiState()
        )

    fun clearLogs() {
        viewModelScope.launch {
            logRepository.clearAllLogs()
        }
    }
}
