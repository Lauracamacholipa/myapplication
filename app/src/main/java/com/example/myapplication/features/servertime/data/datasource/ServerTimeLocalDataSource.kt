// app/src/main/java/com/example/myapplication/features/servertime/data/datasource/ServerTimeLocalDataSource.kt
package com.example.myapplication.features.servertime.data.datasource

import android.content.SharedPreferences
import com.example.myapplication.features.servertime.domain.model.ServerTimeModel
import javax.inject.Inject

class ServerTimeLocalDataSource @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    private companion object {
        const val KEY_SERVER_TIMESTAMP = "server_timestamp"
        const val KEY_LAST_SYNC = "last_sync"
    }

    fun saveServerTime(serverTime: ServerTimeModel) {
        sharedPreferences.edit()
            .putLong(KEY_SERVER_TIMESTAMP, serverTime.timestamp)
            .putLong(KEY_LAST_SYNC, serverTime.lastSync)
            .apply()
    }

    fun getServerTime(): ServerTimeModel? {
        val timestamp = sharedPreferences.getLong(KEY_SERVER_TIMESTAMP, -1L)
        val lastSync = sharedPreferences.getLong(KEY_LAST_SYNC, -1L)

        return if (timestamp != -1L && lastSync != -1L) {
            ServerTimeModel(timestamp, lastSync)
        } else {
            null
        }
    }

    fun getCurrentTime(): Long {
        val savedTime = getServerTime()
        return if (savedTime != null) {
            val timePassed = System.currentTimeMillis() - savedTime.lastSync
            savedTime.timestamp + timePassed
        } else {
            System.currentTimeMillis()
        }
    }
}