package com.example.myapplication.features.profile.domain.model.vo

@JvmInline
value class Username private constructor(val value: String) {
    companion object {
        fun create(raw: String): Username {
            //require(raw.isNullOrBlank()) {
            //    "Name can not be null or blank"
            //}
            val value = raw.trim()
            require(value.isNotEmpty()) {
                "Name can not be empty"
            }
            return Username(value)
        }
    }
}