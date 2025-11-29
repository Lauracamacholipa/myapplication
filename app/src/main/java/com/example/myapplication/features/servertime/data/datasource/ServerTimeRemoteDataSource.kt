// app/src/main/java/com/example/myapplication/features/servertime/data/datasource/ServerTimeRemoteDataSource.kt
package com.example.myapplication.features.servertime.data.datasource

import android.util.Log
import com.example.myapplication.features.servertime.data.api.WorldTimeService
import com.example.myapplication.features.servertime.domain.model.ServerTimeModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ServerTimeRemoteDataSource @Inject constructor(
    private val worldTimeService: WorldTimeService
) {

    fun getServerTime(): Flow<ServerTimeModel> = flow {
        try {
            Log.d("API_DEBUG", "🔄 LLAMANDO a WorldTimeAPI...")
            val response = worldTimeService.getWorldTime()
            Log.d("API_DEBUG", "✅ API RESPONSE: unixtime=${response.unixtime}")

            val serverTime = ServerTimeModel(
                timestamp = response.unixtime * 1000, // Convertir a milliseconds
                lastSync = System.currentTimeMillis()
            )
            Log.d("API_DEBUG", "🕒 HORA SERVIDOR: ${serverTime.timestamp}")
            emit(serverTime)
        } catch (e: Exception) {
            Log.d("API_DEBUG", "❌ FALLÓ WorldTimeAPI: ${e.message}")
            Log.d("API_DEBUG", "📱 Usando HORA LOCAL como fallback")

            // ESTE ES EL PROBLEMA - siempre usa local cuando falla
            val fallbackTime = ServerTimeModel(
                timestamp = System.currentTimeMillis(), // ← ¡ESTA ES LA HORA LOCAL!
                lastSync = System.currentTimeMillis()
            )
            emit(fallbackTime)
        }
    }

    suspend fun updateServerTime(): Flow<ServerTimeModel> = flow {
        try {
            Log.d("API_DEBUG", "🔄 SINCRONIZANDO con WorldTimeAPI...")
            val response = worldTimeService.getWorldTime()
            Log.d("API_DEBUG", "✅ SINCRONIZACIÓN EXITOSA: ${response.unixtime}")

            val serverTime = ServerTimeModel(
                timestamp = response.unixtime * 1000,
                lastSync = System.currentTimeMillis()
            )
            emit(serverTime)
        } catch (e: Exception) {
            Log.d("API_DEBUG", "❌ SINCRONIZACIÓN FALLÓ: ${e.message}")
            throw e // ← IMPORTANTE: No silenciar el error aquí
        }
    }
}