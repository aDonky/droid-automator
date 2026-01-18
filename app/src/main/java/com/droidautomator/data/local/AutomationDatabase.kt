package com.droidautomator.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.droidautomator.data.local.dao.AutomationDao
import com.droidautomator.data.local.dao.LogDao
import com.droidautomator.data.local.entity.AutomationEntity
import com.droidautomator.data.local.entity.LogEntity

@Database(
    entities = [AutomationEntity::class, LogEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AutomationDatabase : RoomDatabase() {
    abstract fun automationDao(): AutomationDao
    abstract fun logDao(): LogDao

    companion object {
        const val DATABASE_NAME = "droid_automator.db"
    }
}
