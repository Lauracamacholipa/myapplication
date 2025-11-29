// app/src/main/java/com/example/myapplication/features/servertime/domain/model/ServerTimeModel.kt
package com.example.myapplication.features.servertime.domain.model

data class ServerTimeModel(
    val timestamp: Long,
    val lastSync: Long = System.currentTimeMillis()
)