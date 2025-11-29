package com.example.myapplication.features.login.domain.model

@JvmInline
value class Password(val value: String) {
    init {
        require(value.isNotEmpty()){
            "Password must not be empty"
        }
    }

    override fun toString(): String = value
}