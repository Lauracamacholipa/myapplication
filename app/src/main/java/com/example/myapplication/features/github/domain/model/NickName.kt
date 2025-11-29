package com.example.myapplication.features.github.domain.model

@JvmInline
value class NickName(val value: String) {
    init{
        require(value.isNotEmpty()){
            "NickName must not be empty"
        }

        require(value.length < 10){
            "Nickname must be less tham 10 characters"
        }

        require(value.length >= 3){
            "NickName must be at least 3 characters"
        }

        require(! value.contains(" ")){
            "Nickname must not contain spaces ' '"
        }
    }

    override fun toString(): String = value
}