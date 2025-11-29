package com.example.myapplication.features.movies.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.develoop.logs.LogApi
import com.example.myapplication.features.github.presentation.GirhubViewModel.GithubStateUI
import com.example.myapplication.features.logs.data.datasource.LogsRemoteDataSource
import com.example.myapplication.features.movies.data.repository.MovieRepository
import com.example.myapplication.features.movies.domain.model.MovieModel
import com.example.myapplication.features.movies.domain.usercases.GetMoviesUseCase
import com.google.protobuf.ByteString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MoviesViewModel(val useCase: GetMoviesUseCase,
                      val repository: MovieRepository): ViewModel() {
    sealed class MoviesUiState{
        object Init : MoviesUiState()
        object Loading : MoviesUiState()
        class Error(val message: String) : MoviesUiState()
        class Success(val movies: List<MovieModel>) : MoviesUiState()
    }

    private val _state = MutableStateFlow<MoviesUiState>(MoviesUiState.Init)
    val state : StateFlow<MoviesUiState> = _state.asStateFlow()

    init {
        observeLocalMovies()
    }

    private fun observeLocalMovies() {
        viewModelScope.launch {
            val logData = LogApi.LogData.newBuilder()
                .setAndroidId(ByteString.copyFromUtf8("abc123"))
                .setAppInstanceId(ByteString.copyFromUtf8("instance_001"))
                .setLogLevel(LogApi.ELogLevel.LEVEL_ERROR)
                .setMessage("Something went wrong")
                .setStackTrace("Stacktrace here...")
                .setServerTimeStamp(System.currentTimeMillis())
                .setMobileTimeStamp(System.currentTimeMillis())
                .setVersionCode(123)
                .setUserId("user_42")
                .build()
            val request = LogApi.LogRequest.newBuilder()
                .addLogs(logData)
                .build()
                val result = LogsRemoteDataSource(
                    "10.0.2.2",
                    port = 9090
                ).send(request)
                println(result)

//            repository.getMoviesFromDb().collect { movies ->
//                if (movies.isNotEmpty()) {
//                    _state.value = MoviesUiState.Success(movies)
//                }
//            }
        }
    }

    fun fetchMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = MoviesUiState.Loading
            val result = useCase.invoke()

            result.fold(
                onSuccess = { list ->
                    _state.value = MoviesUiState.Success(list)
                            },
                onFailure = { err ->
                    _state.value = MoviesUiState.Error(err.message ?: "Error desconocido")
                }
            )
        }
    }

    fun toggleFavourite(movieId: Int, currentFavouriteState: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.toggleFavourite(movieId, !currentFavouriteState)
        }
    }
}