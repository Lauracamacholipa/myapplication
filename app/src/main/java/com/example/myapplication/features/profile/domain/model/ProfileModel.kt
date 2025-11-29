package com.example.myapplication.features.profile.domain.model

import com.example.myapplication.features.github.domain.model.UrlPath
import com.example.myapplication.features.login.domain.model.Email
import com.example.myapplication.features.profile.domain.model.vo.Cellphone
import com.example.myapplication.features.profile.domain.model.vo.Summary
import com.example.myapplication.features.profile.domain.model.vo.Username

class ProfileModel (val pathUrl: UrlPath,
                    val name: Username,
                    val email: Email,
                    val cellphone: Cellphone,
                    val summary: Summary
){
}