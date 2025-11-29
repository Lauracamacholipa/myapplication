package com.example.myapplication.features.dollar.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.features.dollar.data.repository.DollarRepository
import com.example.myapplication.features.dollar.domain.model.DollarModel
import com.example.myapplication.features.dollar.domain.usecases.FetchDollarUseCase
import com.example.myapplication.features.servertime.data.repository.ServerTimeRepository
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DollarViewModel(
    val fetchDollarUseCase: FetchDollarUseCase,
    val repository: DollarRepository,
    val serverTimeRepository: ServerTimeRepository
): ViewModel() {
    sealed class DollarUIState {
        object Loading : DollarUIState()
        class Error(val message: String) : DollarUIState()
        class Success(val data: DollarModel) : DollarUIState()
    }

    suspend fun getToken(): String = suspendCoroutine { continuation ->
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("FIREBASE", "getInstanceId failed", task.exception)
                    continuation.resumeWithException(task.exception ?: Exception("Unknown error"))
                    return@addOnCompleteListener
                }
                val token = task.result
                Log.d("FIREBASE", "FCM Token: $token")
                continuation.resume(token ?: "")
            }
    }

    private val _history = MutableStateFlow<List<DollarModel>>(emptyList())
    val history: StateFlow<List<DollarModel>> = _history.asStateFlow()

    // StateFlow para la fecha
    private val _currentTime = MutableStateFlow<String>("Iniciando...")
    val currentTime: StateFlow<String> = _currentTime.asStateFlow()

    init {
        getDollar()
        startTimeUpdates()
        startServerSync()
    }

    private val _uiState = MutableStateFlow<DollarUIState>(DollarUIState.Loading)
    val uiState: StateFlow<DollarUIState> = _uiState.asStateFlow()

    // ACTUALIZACIÓN EN TIEMPO REAL - VERSIÓN MEJORADA
    private fun startTimeUpdates() {
        viewModelScope.launch(Dispatchers.Default) {
            var updateCount = 0
            while (isActive) {
                try {
                    val currentTimeMillis = System.currentTimeMillis()
                    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
                    val formattedTime = dateFormat.format(Date(currentTimeMillis))

                    // Actualizar en el StateFlow
                    _currentTime.value = formattedTime

                    updateCount++
                    if (updateCount % 10 == 0) { // Log cada 10 segundos para debug
                        Log.d("TimeDebug", "Hora actualizada: $formattedTime")
                    }

                    delay(1000) // Esperar 1 segundo
                } catch (e: Exception) {
                    Log.e("TimeDebug", "Error en actualización: ${e.message}")
                }
            }
        }
    }

    // SINCRONIZACIÓN CON SERVIDOR
    private fun startServerSync() {
        viewModelScope.launch {
            try {
                serverTimeRepository.syncServerTime().collect { serverTime ->
                    Log.d("ServerTime", "✅ Hora servidor sincronizada: ${serverTime.timestamp}")
                }
            } catch (e: Exception) {
                Log.d("ServerTime", "⚠️ Usando hora local: ${e.message}")
            }
        }
    }

    fun getDollar() {
        viewModelScope.launch(Dispatchers.IO) {
            getToken()
            fetchDollarUseCase.invoke().collect { data ->
                _uiState.value = DollarUIState.Success(data)
            }
        }
    }

    // Función para forzar actualización manual
    fun refreshTime() {
        val currentTimeMillis = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        _currentTime.value = dateFormat.format(Date(currentTimeMillis))
    }

    /*fun getHistory() {
        viewModelScope.launch {
            repository.getHistory().collect { list ->
                _history.value = list
            }
        }
    }

    fun deleteDollar(dollar: DollarModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteDollar(dollar)
        }
    }*/
}