package com.example.myapplication.features.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.features.github.domain.usercases.FindByNickNameUseCase
import com.example.myapplication.features.login.domain.model.UserLoginModel
import com.example.myapplication.features.login.domain.usercases.LoginUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LogInViewModel(
    val loginusecase: LoginUseCase
): ViewModel() {
    sealed class LoginStateUI{
        object Init: LoginStateUI()
        object Loading: LoginStateUI()
        data class Success(val user: UserLoginModel) : LoginStateUI()
        data class Error(val message: String): LoginStateUI()
    }
    private val _state = MutableStateFlow<LoginStateUI>(LoginStateUI.Init)
    val state : StateFlow<LoginStateUI> = _state.asStateFlow()

    fun login(username: String, password: String){
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = LoginStateUI.Loading
            val result = loginusecase(username, password)

            result.fold(
                onSuccess = { user -> _state.value = LoginStateUI.Success(user) },
                onFailure = { error -> _state.value = LoginStateUI.Error(error.message ?: "Error desconocido") }
            )
        }
    }
}