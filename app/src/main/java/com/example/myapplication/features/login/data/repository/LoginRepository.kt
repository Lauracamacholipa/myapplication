package com.example.myapplication.features.login.data.repository

import com.example.myapplication.features.login.domain.model.Email
import com.example.myapplication.features.login.domain.model.Password
import com.example.myapplication.features.login.domain.model.UserLoginModel
import com.example.myapplication.features.login.domain.repository.ILoginRepository

class LoginRepository: ILoginRepository {
    override fun login(username: String, password: String): Result<UserLoginModel> {
        // Aquí lo más simple posible: hardcodeamos credenciales
        return if (username == "admin" && password == "1234") {
            Result.success(UserLoginModel(Email(username), Password("fake_token_12345")))
        } else {
            Result.failure(Exception("Credenciales incorrectas"))
        }
    }
}