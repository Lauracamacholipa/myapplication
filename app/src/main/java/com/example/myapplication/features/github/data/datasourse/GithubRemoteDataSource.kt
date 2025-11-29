package com.example.myapplication.features.github.data.datasourse

import com.example.myapplication.features.github.data.api.dto.GithubDto
import com.example.myapplication.features.github.data.api.GithubService

//llama a la interfaz girhubService

class GithubRemoteDataSource(val githubService: GithubService) {
    suspend fun getUser(nick: String): Result<GithubDto> {
        val response = githubService.getInfoAvatar(nick)
        if (response.isSuccessful) {
            return Result.success(response.body()!!)
        } else {
            return Result.failure(Exception("Error al obtener el usuario"))
        }
    }
}