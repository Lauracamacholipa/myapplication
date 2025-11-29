package com.example.myapplication.features.movies.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "poster_url")
    val posterUrl: String,

    @ColumnInfo(name = "timestamp")
    val timestamp: String? = "",

    @ColumnInfo(name = "overview")
    val overview: String,

    @ColumnInfo(name = "favourite")
    val favourite: Int = 0
)