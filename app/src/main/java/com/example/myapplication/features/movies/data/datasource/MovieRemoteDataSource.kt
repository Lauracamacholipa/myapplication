package com.example.myapplication.features.movies.data.datasource

import android.content.Context
import com.example.myapplication.R
import com.example.myapplication.features.movies.data.api.MovieService
import com.example.myapplication.features.movies.data.api.dto.MoviePageDto

class MovieRemoteDataSource(val movieServidce: MovieService, val context: Context) {
    suspend fun getMovies(): Result<MoviePageDto> {
        val response = movieServidce.getMovies(apiKey = context.getString(R.string.api_key_movies))
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                return Result.success(body)
            } else {
                return Result.failure(Exception("Respuesta vacía del servidor"))
            }
        } else {
            // Log detallado
            val errorBody = response.errorBody()?.string()
            return Result.failure(
                Exception("Error ${response.code()} - ${response.message()} - $errorBody")
            )
        }
    }
}