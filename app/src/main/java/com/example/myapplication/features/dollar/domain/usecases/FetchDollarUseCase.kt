package com.example.myapplication.features.dollar.domain.usecases

import com.example.myapplication.features.dollar.data.repository.DollarRepository
import com.example.myapplication.features.dollar.domain.model.DollarModel
import com.example.myapplication.features.dollar.domain.repository.IDollarRepository
import kotlinx.coroutines.flow.Flow

class FetchDollarUseCase(val repository: IDollarRepository) {
    suspend fun invoke(): Flow<DollarModel> {
        return repository.getDollar()
    }
}