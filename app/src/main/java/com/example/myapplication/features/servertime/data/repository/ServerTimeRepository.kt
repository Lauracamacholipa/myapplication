// app/src/main/java/com/example/myapplication/features/servertime/data/repository/ServerTimeRepository.kt
package com.example.myapplication.features.servertime.data.repository

import com.example.myapplication.features.servertime.data.datasource.ServerTimeLocalDataSource
import com.example.myapplication.features.servertime.data.datasource.SimulatedServerDataSource
import com.example.myapplication.features.servertime.domain.model.ServerTimeModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ServerTimeRepository @Inject constructor(
    private val remoteDataSource: SimulatedServerDataSource,
    private val localDataSource: ServerTimeLocalDataSource
) {

    // CAMBIAR: Hacer esta función suspend
    suspend fun getServerTime(): Flow<ServerTimeModel> {
        return remoteDataSource.getServerTime()
            .map { serverTime ->
                localDataSource.saveServerTime(serverTime)
                serverTime
            }
    }

    // CAMBIAR: Hacer esta función suspend
    suspend fun getCurrentTime(): Flow<Long> {
        return remoteDataSource.getServerTime()
            .map { serverTime ->
                localDataSource.saveServerTime(serverTime)
                localDataSource.getCurrentTime()
            }
    }

    // Esta función NO cambia - no usa el servidor remoto
    fun getLocalTime(): Long {
        return localDataSource.getCurrentTime()
    }

    // Esta ya era suspend - se mantiene igual
    suspend fun syncServerTime(): Flow<ServerTimeModel> {
        return remoteDataSource.syncServerTime()
            .map { serverTime ->
                localDataSource.saveServerTime(serverTime)
                serverTime
            }
    }
}