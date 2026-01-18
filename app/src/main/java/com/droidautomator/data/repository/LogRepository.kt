package com.droidautomator.data.repository

import com.droidautomator.data.local.dao.LogDao
import com.droidautomator.data.local.entity.LogEntity
import com.droidautomator.domain.model.LogEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LogRepository @Inject constructor(
    private val logDao: LogDao
) {

    fun getAllLogs(): Flow<List<LogEntry>> {
        return logDao.getAllLogs().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    fun getRecentLogs(limit: Int = 100): Flow<List<LogEntry>> {
        return logDao.getRecentLogs(limit).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    fun getLogsForAutomation(automationId: Long): Flow<List<LogEntry>> {
        return logDao.getLogsForAutomation(automationId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun logExecution(
        automationId: Long,
        automationName: String,
        triggerData: String,
        success: Boolean,
        message: String
    ) {
        val entity = LogEntity(
            automationId = automationId,
            automationName = automationName,
            timestamp = System.currentTimeMillis(),
            triggerData = triggerData,
            success = success,
            message = message
        )
        logDao.insertLog(entity)
    }

    suspend fun clearAllLogs() {
        logDao.clearAllLogs()
    }

    suspend fun clearLogsForAutomation(automationId: Long) {
        logDao.clearLogsForAutomation(automationId)
    }

    suspend fun deleteOldLogs(daysOld: Int = 30) {
        val cutoffTime = System.currentTimeMillis() - (daysOld * 24 * 60 * 60 * 1000L)
        logDao.deleteOldLogs(cutoffTime)
    }

    private fun LogEntity.toDomain(): LogEntry {
        return LogEntry(
            id = id,
            automationId = automationId,
            automationName = automationName,
            timestamp = timestamp,
            triggerData = triggerData,
            success = success,
            message = message
        )
    }
}
