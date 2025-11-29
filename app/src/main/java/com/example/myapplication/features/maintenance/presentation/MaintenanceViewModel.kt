package com.example.myapplication.features.maintenance.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.features.maintenance.data.MaintenanceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MaintenanceViewModel(private val repository: MaintenanceRepository): ViewModel() {
    private val _maintenanceMode = MutableStateFlow(false)
    val maintenanceMode: StateFlow<Boolean> = _maintenanceMode.asStateFlow()

    init {
        observeMaintenanceStatus()
        startPeriodicFetch()
    }

    private fun observeMaintenanceStatus() {
        viewModelScope.launch {
            repository.observeMaintenanceStatus().collect { isMaintenance ->
                _maintenanceMode.value = isMaintenance
                Log.d("MaintenanceViewModel", "Maintenance status observado: $isMaintenance")
            }
        }
    }

    private fun startPeriodicFetch() {
        viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                try {
                    fetchMaintenanceStatus()
                    delay(10000) // Fetch cada 10 segundos (puedes cambiar a 30000 para 30 segundos)
                } catch (e: Exception) {
                    Log.e("MaintenanceViewModel", "Error en periodic fetch", e)
                    delay(10000)
                }
            }
        }
    }

    private suspend fun fetchMaintenanceStatus() {
        try {
            repository.fetchMaintenanceStatus().collect { isMaintenance ->
                _maintenanceMode.value = isMaintenance
                Log.d("MaintenanceViewModel", "Fetch periódico completado: $isMaintenance")
            }
        } catch (e: Exception) {
            Log.e("MaintenanceViewModel", "Error en fetchMaintenanceStatus", e)
        }
    }

    fun refreshMaintenanceStatus() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchMaintenanceStatus()
        }
    }
}