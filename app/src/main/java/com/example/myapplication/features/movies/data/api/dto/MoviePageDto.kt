package com.example.myapplication.features.movies.data.api.dto

import com.google.gson.annotations.SerializedName

data class MoviePageDto(@SerializedName("page") val page: Int,
                   @SerializedName("results") val results: List<MovieDto>) {
}