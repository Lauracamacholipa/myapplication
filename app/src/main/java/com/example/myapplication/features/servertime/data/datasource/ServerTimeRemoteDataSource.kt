// app/src/main/java/com/example/myapplication/features/servertime/data/datasource/ServerTimeRemoteDataSource.kt
package com.example.myapplication.features.servertime.data.datasource

import com.example.myapplication.features.servertime.domain.model.ServerTimeModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class ServerTimeRemoteDataSource @Inject constructor() {

    fun getServerTime(): Flow<ServerTimeModel> = callbackFlow {
        val callback = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val serverTimestamp = snapshot.getValue(Long::class.java) ?: System.currentTimeMillis()
                val serverTime = ServerTimeModel(
                    timestamp = serverTimestamp,
                    lastSync = System.currentTimeMillis()
                )
                trySend(serverTime)
            }
        }

        val database = com.google.firebase.Firebase.database
        val timeRef = database.getReference("server_time")
        timeRef.addValueEventListener(callback)

        awaitClose {
            timeRef.removeEventListener(callback)
        }
    }

    suspend fun updateServerTime(): Flow<ServerTimeModel> = callbackFlow {
        val currentTime = System.currentTimeMillis()
        val database = com.google.firebase.Firebase.database
        val timeRef = database.getReference("server_time")

        timeRef.setValue(currentTime).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val serverTime = ServerTimeModel(
                    timestamp = currentTime,
                    lastSync = System.currentTimeMillis()
                )
                trySend(serverTime)
                close()
            } else {
                close(task.exception ?: Exception("Failed to update server time"))
            }
        }

        awaitClose { }
    }
}