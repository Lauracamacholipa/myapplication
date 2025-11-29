package com.example.myapplication.features.movies.data.repository

import com.example.myapplication.features.dollar.data.datasource.DollarLocalDataSource
import com.example.myapplication.features.movies.data.api.dto.MoviePageDto
import com.example.myapplication.features.movies.data.datasource.MovieLocalDataSource
import com.example.myapplication.features.movies.data.datasource.MovieRemoteDataSource
import com.example.myapplication.features.movies.domain.model.MovieModel
import com.example.myapplication.features.movies.domain.repository.IMovieRepository
import kotlinx.coroutines.flow.Flow

class MovieRepository(val remoteDatasource: MovieRemoteDataSource,
                      val localDataSource: MovieLocalDataSource): IMovieRepository {
    override suspend fun getMovies(): Result<List<MovieModel>> {
        val response = remoteDatasource.getMovies()

        return response.fold(
            onSuccess = { pageDto ->
                val movies = pageDto.results.map {
                    MovieModel(
                        title = it.title,
                        posterURL = "https://image.tmdb.org/t/p/w185${it.poster_path}",
                        overview = it.overview,
                        favourite = false
                    )
                }

                val ids = pageDto.results.mapIndexed { _, dto -> dto.hashCode() }
                localDataSource.saveMovies(movies, ids)

                Result.success(movies)
            },
            onFailure = { error ->
                Result.failure(error)
            }
        )
    }

    fun getMoviesFromDb(): Flow<List<MovieModel>> {
        return localDataSource.getMovies()
    }

    suspend fun toggleFavourite(movieId: Int, isFavourite: Boolean) {
        localDataSource.toggleFavourite(movieId, isFavourite)
    }

    fun getFavouriteMovies(): Flow<List<MovieModel>> {
        return localDataSource.getFavouriteMovies()
    }
}