package com.example.myapplication.features.maintenance.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.maintenanceDataStore by preferencesDataStore(name = "maintenance_preferences")
class MaintenanceDataStore(private val context: Context) {
    companion object {
        val MAINTENANCE_MODE = booleanPreferencesKey("maintenance_mode")
    }

    suspend fun setMaintenanceMode(isUnderMaintenance: Boolean) {
        context.maintenanceDataStore.edit { preferences ->
            preferences[MAINTENANCE_MODE] = isUnderMaintenance
        }
    }

    fun getMaintenanceMode(): Flow<Boolean> {
        return context.maintenanceDataStore.data.map { preferences ->
            preferences[MAINTENANCE_MODE] ?: false
        }
    }
}