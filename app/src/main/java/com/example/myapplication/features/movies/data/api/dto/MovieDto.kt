package com.example.myapplication.features.movies.data.api.dto

import com.google.gson.annotations.SerializedName

data class MovieDto(@SerializedName("title") val title: String,
               @SerializedName("poster_path") val poster_path: String,
                @SerializedName("overview") val overview: String) {
}