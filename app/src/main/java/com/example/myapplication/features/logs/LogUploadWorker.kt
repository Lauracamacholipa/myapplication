package com.example.myapplication.features.logs

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.myapplication.features.movies.domain.usercases.GetMoviesUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LogUploadWorker(
    appContext: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(appContext, workerParameters) , KoinComponent {


    private val fetchPopularMoviesUseCase: GetMoviesUseCase by inject()


    override suspend fun doWork(): Result {


        println("ejecutar instrucción para subir datos")
        val response = fetchPopularMoviesUseCase.invoke()
        response.fold(
            onFailure = {
                return Result.failure()
            },
            onSuccess = {
                println("datos subidos ${it.size}")
                return Result.success()
            }
        )


    }
}

