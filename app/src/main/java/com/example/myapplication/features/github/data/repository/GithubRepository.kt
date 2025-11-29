package com.example.myapplication.features.github.data.repository

import com.example.myapplication.features.github.data.datasourse.GithubRemoteDataSource
import com.example.myapplication.features.github.domain.model.NickName
import com.example.myapplication.features.github.domain.model.UrlPath
import com.example.myapplication.features.github.domain.model.UserModel
import com.example.myapplication.features.github.domain.repository.IgithubRepository

class GithubRepository(val remoteDataSource: GithubRemoteDataSource
): IgithubRepository {
    override suspend fun findbyNickName(value: String): Result<UserModel> {
        val response = remoteDataSource.getUser(value)

        response.fold(
            onSuccess = {
                    it ->
                return Result.success(UserModel(
                    nickName = NickName( it.login),
                    pathURL = UrlPath(it.url)
                ))
            },
            onFailure = {
                return Result.failure(it)
            }
        )
    }
}

