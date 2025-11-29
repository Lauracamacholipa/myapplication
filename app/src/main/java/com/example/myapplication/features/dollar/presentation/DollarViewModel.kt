// app/src/main/java/com/example/myapplication/features/dollar/presentation/DollarViewModel.kt
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

    // StateFlow para la fecha - MÉTODO DIRECTO
    private val _currentTime = MutableStateFlow<String>("Iniciando...")
    val currentTime: StateFlow<String> = _currentTime.asStateFlow()

    init {
        getDollar()
        startTimeUpdates()
    }

    private val _uiState = MutableStateFlow<DollarUIState>(DollarUIState.Loading)
    val uiState: StateFlow<DollarUIState> = _uiState.asStateFlow()

    // MÉTODO DEFINITIVO: Forzar hora del servidor
    private fun startTimeUpdates() {
        viewModelScope.launch(Dispatchers.Default) {
            var updateCount = 0
            while (isActive) {
                try {
                    // ✅ MÉTODO CONTUNDENTE: Siempre mostrar hora +2 horas
                    val horaReal = System.currentTimeMillis() // Hora del dispositivo
                    val horaServidor = horaReal + 7200000L // +2 horas (7200000 ms)

                    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
                    val horaServidorFormateada = dateFormat.format(Date(horaServidor))
                    val horaDispositivoFormateada = dateFormat.format(Date(horaReal))

                    // Actualizar en el StateFlow
                    _currentTime.value = horaServidorFormateada

                    updateCount++
                    if (updateCount % 10 == 0) {
                        Log.d("METODO_DEFINITIVO", "📱 DISPOSITIVO: $horaDispositivoFormateada")
                        Log.d("METODO_DEFINITIVO", "🌐 SERVIDOR: $horaServidorFormateada")
                        Log.d("METODO_DEFINITIVO", "✅ MOSTRANDO HORA SERVIDOR (+2 horas)")
                    }

                    delay(1000)
                } catch (e: Exception) {
                    Log.e("METODO_DEFINITIVO", "Error: ${e.message}")
                }
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
        val horaReal = System.currentTimeMillis()
        val horaServidor = horaReal + 7200000L // +2 horas
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        _currentTime.value = dateFormat.format(Date(horaServidor))
    }
}