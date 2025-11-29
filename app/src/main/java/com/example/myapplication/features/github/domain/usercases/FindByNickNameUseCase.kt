package com.example.myapplication.features.github.domain.usercases

import com.example.myapplication.features.github.domain.model.UserModel
import com.example.myapplication.features.github.domain.repository.IgithubRepository

class FindByNickNameUseCase(val repository: IgithubRepository) {
    suspend fun invoke(nickName: String): Result<UserModel>{
        return repository.findbyNickName(nickName)
    }
}