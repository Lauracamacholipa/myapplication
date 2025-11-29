package com.example.myapplication.features.maintenance.data
import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.example.myapplication.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class MaintenanceRepository(
    private val remoteConfig: FirebaseRemoteConfig,
    private val dataStore: MaintenanceDataStore
) {
    init {
        setupRemoteConfig()
    }

    private fun setupRemoteConfig() {
        try {
            val configSettings = FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(60) // Fetch cada minuto
                .build()
            remoteConfig.setConfigSettingsAsync(configSettings)
            remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
            Log.d("MaintenanceRepository", "Remote Config iniciado correctamente")
        } catch (e: Exception) {
            Log.e("MaintenanceRepository", "Error al iniciar Remote Config", e)
        }
    }

    suspend fun fetchMaintenanceStatus(): Flow<Boolean> = flow {
        try {
            remoteConfig.fetchAndActivate().await()
            val maintenanceMode = remoteConfig.getBoolean("maintenance_mode")

            Log.d("MaintenanceRepository", "Maintenance Mode obtenido: $maintenanceMode")

            dataStore.setMaintenanceMode(maintenanceMode)
            emit(maintenanceMode)
        } catch (e: Exception) {
            Log.e("MaintenanceRepository", "Error al obtener maintenance status", e)
            dataStore.getMaintenanceMode().collect { emit(it) }
        }
    }

    fun observeMaintenanceStatus(): Flow<Boolean> {
        return dataStore.getMaintenanceMode()
    }
}