package com.example.myapplication.features.movies.domain.repository

import com.example.myapplication.features.movies.domain.model.MovieModel

interface IMovieRepository {
    suspend fun getMovies(): Result<List<MovieModel>>
}