package com.example.myapplication.features.dollar.data.repository

import com.example.myapplication.features.dollar.data.datasource.DollarLocalDataSource
import com.example.myapplication.features.dollar.data.datasource.RealTimeRemoteDataSource
import com.example.myapplication.features.dollar.domain.model.DollarModel
import com.example.myapplication.features.dollar.domain.repository.IDollarRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach

class DollarRepository(val realTimeRemoteDataSource: RealTimeRemoteDataSource,
                       val localDataSource: DollarLocalDataSource): IDollarRepository {
    override suspend fun getDollar(): Flow<DollarModel> {
        //return flow {
        //    emit(DollarModel("6.96", "12.6"))
        //}
        return realTimeRemoteDataSource.getDollarUpdates()
            .onEach {
                localDataSource.insert(it)
            }
    }

    /*fun getHistory(): Flow<List<DollarModel>> {
        return localDataSource.getHistory()
    }

    suspend fun deleteDollar(dollar: DollarModel) {
        localDataSource.delete(dollar)
    }*/
}