package com.example.myapplication.features.dollar.data.datasource

import com.example.myapplication.features.dollar.data.database.dao.IDollarDao
import com.example.myapplication.features.dollar.data.mapper.toEntity
import com.example.myapplication.features.dollar.data.mapper.toModel
import com.example.myapplication.features.dollar.domain.model.DollarModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DollarLocalDataSource(
    val dao: IDollarDao
) {
    suspend fun getList(): List<DollarModel> {
        return dao.getList().map {
            it.toModel()
        }
    }
    suspend fun deleteAll() {
        dao.deleteAll()
    }
    suspend fun inserTDollars(list: List<DollarModel>) {
        val dollarEntity = list.map { it.toEntity() }
        dao.insertDollars(dollarEntity)
    }

    suspend fun insert(dollar: DollarModel) {
        dao.insert(dollar.toEntity())
    }

    fun getHistory(): Flow<List<DollarModel>> {
        return dao.getHistory().map { list ->
            list.map { it.toModel() }
        }
    }

    suspend fun delete(dollar: DollarModel) {
        dao.delete(dollar.toEntity())
    }
}