package com.example.myapplication.features.dollar.data.mapper

import com.example.myapplication.features.dollar.data.database.entity.DollarEntity
import com.example.myapplication.features.dollar.domain.model.DollarModel

fun DollarEntity.toModel() : DollarModel {
    return DollarModel(
        dollarOfficial = dollarOfficial,
        dollarParalelo = dollarParalelo,
        usdt = usdt,
        usdc = usdc
    )
}

fun DollarModel.toEntity() : DollarEntity {
    return DollarEntity(
        dollarOfficial = dollarOfficial,
        dollarParalelo = dollarParalelo,
        usdt = usdt,
        usdc = usdc,
        timestamp = System.currentTimeMillis())
}
