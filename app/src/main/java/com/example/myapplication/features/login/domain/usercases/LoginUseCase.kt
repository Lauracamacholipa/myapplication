package com.example.myapplication.features.login.domain.usercases

import com.example.myapplication.features.login.domain.model.UserLoginModel
import com.example.myapplication.features.login.domain.repository.ILoginRepository

class LoginUseCase(private val repository: ILoginRepository) {
    operator fun invoke(username: String, password: String): Result<UserLoginModel> {
        return repository.login(username, password)
    }
}