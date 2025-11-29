package com.example.myapplication.features.github.domain.model

@JvmInline
value class UrlPath(val value: String) {
    init {
        require(value.isNotEmpty()){
            "UrlPath must not be empty"
        }

        require(value.startsWith("https://")){
            "UlrPath must start with 'https'"
        }
    }

    override fun toString(): String = value
}