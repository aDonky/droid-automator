package com.droidautomator.di

import android.content.Context
import androidx.room.Room
import com.droidautomator.data.local.AutomationDatabase
import com.droidautomator.data.local.dao.AutomationDao
import com.droidautomator.data.local.dao.LogDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AutomationDatabase {
        return Room.databaseBuilder(
            context,
            AutomationDatabase::class.java,
            AutomationDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideAutomationDao(database: AutomationDatabase): AutomationDao {
        return database.automationDao()
    }

    @Provides
    fun provideLogDao(database: AutomationDatabase): LogDao {
        return database.logDao()
    }
}
