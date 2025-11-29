// app/src/main/java/com/example/myapplication/features/servertime/data/repository/ServerTimeRepository.kt
package com.example.myapplication.features.servertime.data.repository

import com.example.myapplication.features.servertime.data.datasource.ServerTimeLocalDataSource
import com.example.myapplication.features.servertime.data.datasource.ServerTimeRemoteDataSource
import com.example.myapplication.features.servertime.domain.model.ServerTimeModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ServerTimeRepository @Inject constructor(
    private val remoteDataSource: ServerTimeRemoteDataSource,
    private val localDataSource: ServerTimeLocalDataSource
) {

    fun getServerTime(): Flow<ServerTimeModel> {
        return remoteDataSource.getServerTime()
            .map { serverTime ->
                localDataSource.saveServerTime(serverTime)
                serverTime
            }
    }

    fun getCurrentTime(): Flow<Long> {
        return remoteDataSource.getServerTime()
            .map { serverTime ->
                localDataSource.saveServerTime(serverTime)
                localDataSource.getCurrentTime()
            }
    }

    fun getLocalTime(): Long {
        return localDataSource.getCurrentTime()
    }

    suspend fun syncServerTime(): Flow<ServerTimeModel> {
        return remoteDataSource.updateServerTime()
            .map { serverTime ->
                localDataSource.saveServerTime(serverTime)
                serverTime
            }
    }
}