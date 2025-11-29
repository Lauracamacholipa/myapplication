package com.example.myapplication.features.github.data.api

import com.example.myapplication.features.github.data.api.dto.GithubDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

//contrato de como consumir un servicio

interface GithubService {
    @GET("/users/{githubLogin}")
    suspend fun getInfoAvatar(@Path("githubLogin") githubLogin: String): Response<GithubDto>
}