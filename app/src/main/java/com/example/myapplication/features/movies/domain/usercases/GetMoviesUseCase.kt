package com.example.myapplication.features.movies.domain.usercases

import com.example.myapplication.features.movies.domain.model.MovieModel
import com.example.myapplication.features.movies.domain.repository.IMovieRepository

class GetMoviesUseCase(val repository: IMovieRepository) {
    suspend fun invoke(): Result<List<MovieModel>>{
        return repository.getMovies()
    }
}