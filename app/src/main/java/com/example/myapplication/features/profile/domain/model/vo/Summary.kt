package com.example.myapplication.features.profile.domain.model.vo

class Summary private constructor(val value: String){
    companion object {
        fun create(raw: String) : Summary{
            //require(raw.isNullOrBlank()) {
            //    "Summary can not be null or blank"
            //}
            val value = raw.trim()
            require(value.isNotEmpty()) {
                "Summary can not be empty"
            }
            return Summary(value)
        }
    }
}