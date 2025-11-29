package com.example.myapplication.features.github.domain.repository

import com.example.myapplication.features.github.domain.model.UserModel

interface IgithubRepository {
    suspend fun findbyNickName(value: String): Result<UserModel>
}