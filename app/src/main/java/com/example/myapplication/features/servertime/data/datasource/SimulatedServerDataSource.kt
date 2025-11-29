// app/src/main/java/com/example/myapplication/features/servertime/data/datasource/SimulatedServerDataSource.kt
package com.example.myapplication.features.servertime.data.datasource

import com.example.myapplication.features.servertime.domain.model.ServerTimeModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SimulatedServerDataSource @Inject constructor() {

    suspend fun getServerTime(): Flow<ServerTimeModel> = flow {
        // SERVIDOR SIMULADO - siempre 2 horas adelante
        val serverTime = System.currentTimeMillis() + 7200000L // 2 horas

        val serverTimeModel = ServerTimeModel(
            timestamp = serverTime,
            lastSync = System.currentTimeMillis()
        )
        emit(serverTimeModel)
    }

    suspend fun syncServerTime(): Flow<ServerTimeModel> = flow {
        val serverTime = System.currentTimeMillis() + 7200000L // 2 horas

        val serverTimeModel = ServerTimeModel(
            timestamp = serverTime,
            lastSync = System.currentTimeMillis()
        )
        emit(serverTimeModel)
    }
}