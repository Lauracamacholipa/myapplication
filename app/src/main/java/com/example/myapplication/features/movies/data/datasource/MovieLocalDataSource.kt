package com.example.myapplication.features.movies.data.datasource

import com.example.myapplication.features.movies.data.database.dao.IMovieDao
import com.example.myapplication.features.movies.data.mapper.toEntity
import com.example.myapplication.features.movies.data.mapper.toModel
import com.example.myapplication.features.movies.domain.model.MovieModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MovieLocalDataSource(val dao: IMovieDao) {
    fun getMovies(): Flow<List<MovieModel>> {
        return dao.getAllMovies().map { list -> list.map { it.toModel() } }
    }

    suspend fun saveMovies(movies: List<MovieModel>, ids: List<Int>) {
        val entities = movies.mapIndexed { index, model ->
            model.toEntity(ids[index])
        }
        dao.insertMovies(entities)
    }

    suspend fun toggleFavourite(movieId: Int, isFavourite: Boolean) {
        dao.updateFavourite(movieId, if (isFavourite) 1 else 0)
    }

    fun getFavouriteMovies(): Flow<List<MovieModel>> {
        return dao.getFavouriteMovies().map { list -> list.map { it.toModel() } }
    }
}