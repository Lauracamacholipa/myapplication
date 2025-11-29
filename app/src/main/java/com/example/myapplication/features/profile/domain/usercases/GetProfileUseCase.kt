package com.example.myapplication.features.profile.domain.usercases

import com.example.myapplication.features.profile.domain.model.ProfileModel
import com.example.myapplication.features.profile.domain.repository.IProfileRepository
import kotlinx.coroutines.delay

class GetProfileUseCase( val repository: IProfileRepository) {
    suspend fun invoke(): Result<ProfileModel> {
        return repository.fetchData()
    }
}