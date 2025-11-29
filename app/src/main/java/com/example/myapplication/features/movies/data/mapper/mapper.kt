package com.example.myapplication.features.movies.data.mapper

import com.example.myapplication.features.movies.data.database.entity.MovieEntity
import com.example.myapplication.features.movies.domain.model.MovieModel

fun MovieEntity.toModel(): MovieModel {
    val posterURL = ""
    return MovieModel(
        id = id,
        title = title,
        posterURL = posterURL,
        overview = overview,
        favourite = favourite == 1)
}

fun MovieModel.toEntity(id: Int): MovieEntity {
    return MovieEntity(
        id = id,
        title = title,
        posterUrl = posterURL,
        overview = overview,
        favourite = if (favourite) 1 else 0)
}