package com.example.myapplication.features.dollar.domain.repository

import com.example.myapplication.features.dollar.domain.model.DollarModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface IDollarRepository {
    suspend fun getDollar(): Flow<DollarModel>
}