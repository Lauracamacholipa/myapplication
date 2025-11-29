package com.example.myapplication.features.profile.data.repository

import com.example.myapplication.features.github.domain.model.UrlPath
import com.example.myapplication.features.login.domain.model.Email
import com.example.myapplication.features.profile.domain.model.ProfileModel
import com.example.myapplication.features.profile.domain.model.vo.Cellphone
import com.example.myapplication.features.profile.domain.model.vo.Summary
import com.example.myapplication.features.profile.domain.model.vo.Username
import com.example.myapplication.features.profile.domain.repository.IProfileRepository

class ProfileRepository: IProfileRepository {
    override fun fetchData(): Result<ProfileModel> {
        return Result.success(
            ProfileModel(
                name = Username.create("Homero J. Simpson"),
                email = Email.create("homero.simpson@springfieldmail.com"),
                cellphone = Cellphone("+1 (939) 555‑7422"),
                pathUrl = UrlPath("https://imtexpublicidad.mx/wp-content/uploads/2021/04/Como-ser-xitoso-como-Homero-Simpson.jpeg"),
                summary = Summary.create("Ciudadano de Springfield y dedicado inspector de seguridad en la Planta Nuclear.")
            )
        )
    }
}