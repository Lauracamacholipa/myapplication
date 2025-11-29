package com.example.myapplication.features.profile.domain.repository

import com.example.myapplication.features.profile.domain.model.ProfileModel

interface IProfileRepository {
    fun fetchData(): Result<ProfileModel>
}