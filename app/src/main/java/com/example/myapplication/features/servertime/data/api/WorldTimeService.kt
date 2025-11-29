// app/src/main/java/com/example/myapplication/features/servertime/data/api/WorldTimeService.kt
package com.example.myapplication.features.servertime.data.api

import retrofit2.http.GET

interface WorldTimeService {
    @GET("https://worldtimeapi.org/api/timezone/America/La_Paz") // CAMBIAR a HTTPS
    suspend fun getWorldTime(): WorldTimeResponse
}

data class WorldTimeResponse(
    val unixtime: Long,
    val datetime: String,
    val timezone: String
)