package com.example.myapplication.features.github.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.features.github.domain.model.UserModel
import com.example.myapplication.features.github.domain.usercases.FindByNickNameUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GirhubViewModel(
    val usecase: FindByNickNameUseCase
): ViewModel() {
    sealed class GithubStateUI{
        object Init: GithubStateUI()
        object Loading: GithubStateUI()
        class Error(val message: String): GithubStateUI()
        class Success(val github: UserModel): GithubStateUI()
    }
    private val _state = MutableStateFlow<GithubStateUI>(GithubStateUI.Init)
    val state : StateFlow<GithubStateUI> = _state.asStateFlow()

    fun fetchAlias(nickname: String){
        viewModelScope.launch(context = Dispatchers.IO) {
            _state.value = GithubStateUI.Loading
            val result = usecase.invoke(nickname)

            result.fold(
                onSuccess = {
                    user -> _state.value = GithubStateUI.Success(user)
                },
                onFailure = {
                    error -> _state.value = GithubStateUI.Error(message = error.message ?: "Error desconocido")
                }
            )
        }
    }
}