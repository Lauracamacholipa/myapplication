package com.example.myapplication.features.login.domain.repository

import com.example.myapplication.features.login.domain.model.UserLoginModel

interface ILoginRepository {
    fun login(username: String, password: String): Result<UserLoginModel>
}