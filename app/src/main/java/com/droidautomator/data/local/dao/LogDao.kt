package com.droidautomator.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.droidautomator.data.local.entity.LogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LogDao {

    @Query("SELECT * FROM logs ORDER BY timestamp DESC")
    fun getAllLogs(): Flow<List<LogEntity>>

    @Query("SELECT * FROM logs ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentLogs(limit: Int): Flow<List<LogEntity>>

    @Query("SELECT * FROM logs WHERE automationId = :automationId ORDER BY timestamp DESC")
    fun getLogsForAutomation(automationId: Long): Flow<List<LogEntity>>

    @Insert
    suspend fun insertLog(log: LogEntity): Long

    @Query("DELETE FROM logs")
    suspend fun clearAllLogs()

    @Query("DELETE FROM logs WHERE automationId = :automationId")
    suspend fun clearLogsForAutomation(automationId: Long)

    @Query("DELETE FROM logs WHERE timestamp < :beforeTimestamp")
    suspend fun deleteOldLogs(beforeTimestamp: Long)
}
