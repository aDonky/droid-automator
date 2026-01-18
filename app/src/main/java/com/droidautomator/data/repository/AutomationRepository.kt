package com.droidautomator.data.repository

import com.droidautomator.data.local.converter.Converters
import com.droidautomator.data.local.dao.AutomationDao
import com.droidautomator.data.local.entity.AutomationEntity
import com.droidautomator.domain.model.Automation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AutomationRepository @Inject constructor(
    private val automationDao: AutomationDao
) {

    fun getAllAutomations(): Flow<List<Automation>> {
        return automationDao.getAllAutomations().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    fun getEnabledAutomations(): Flow<List<Automation>> {
        return automationDao.getEnabledAutomations().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun getEnabledAutomationsSync(): List<Automation> {
        return automationDao.getEnabledAutomationsSync().map { it.toDomain() }
    }

    suspend fun getAutomationById(id: Long): Automation? {
        return automationDao.getAutomationById(id)?.toDomain()
    }

    fun getAutomationByIdFlow(id: Long): Flow<Automation?> {
        return automationDao.getAutomationByIdFlow(id).map { it?.toDomain() }
    }

    suspend fun saveAutomation(automation: Automation): Long {
        val entity = automation.toEntity()
        return automationDao.insertAutomation(entity)
    }

    suspend fun updateAutomation(automation: Automation) {
        automationDao.updateAutomation(automation.toEntity())
    }

    suspend fun deleteAutomation(id: Long) {
        automationDao.deleteAutomationById(id)
    }

    suspend fun setEnabled(id: Long, enabled: Boolean) {
        automationDao.setEnabled(id, enabled)
    }

    private fun AutomationEntity.toDomain(): Automation {
        return Automation(
            id = id,
            name = name,
            enabled = enabled,
            trigger = Converters.jsonToTrigger(triggerJson),
            actions = Converters.jsonToActions(actionsJson),
            createdAt = createdAt
        )
    }

    private fun Automation.toEntity(): AutomationEntity {
        return AutomationEntity(
            id = id,
            name = name,
            enabled = enabled,
            triggerJson = Converters.triggerToJson(trigger),
            actionsJson = Converters.actionsToJson(actions),
            createdAt = createdAt
        )
    }
}
