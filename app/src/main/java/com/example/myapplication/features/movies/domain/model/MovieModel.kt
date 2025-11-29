package com.example.myapplication.features.movies.domain.model

import kotlinx.serialization.Serializable

@Serializable
class MovieModel(val id: Int = 0,
                 val title: String,
                 val posterURL: String,
                 val overview: String,
                 val favourite: Boolean = false) {
}