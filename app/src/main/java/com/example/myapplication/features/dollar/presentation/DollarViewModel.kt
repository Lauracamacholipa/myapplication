package com.example.myapplication.features.dollar.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.features.dollar.data.repository.DollarRepository
import com.example.myapplication.features.dollar.domain.model.DollarModel
import com.example.myapplication.features.dollar.domain.usecases.FetchDollarUseCase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class DollarViewModel(val fetchDollarUseCase: FetchDollarUseCase,
                      val repository: DollarRepository): ViewModel() {
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
                // Si la tarea fue exitosa, se obtiene el token
                val token = task.result
                Log.d("FIREBASE", "FCM Token: $token")


                // Reanudar la ejecución con el token
                continuation.resume(token ?: "")
            }
    }

    private val _history = MutableStateFlow<List<DollarModel>>(emptyList())
    val history: StateFlow<List<DollarModel>> = _history.asStateFlow()

    init {
        getDollar()
        //getHistory()
    }

    private val _uiState = MutableStateFlow<DollarUIState>(DollarUIState.Loading)
    val uiState: StateFlow<DollarUIState> = _uiState.asStateFlow()


    fun getDollar() {
        viewModelScope.launch(Dispatchers.IO) {
            getToken()
            fetchDollarUseCase.invoke().collect {
                    data -> _uiState.value = DollarUIState.Success(data) }
        }
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