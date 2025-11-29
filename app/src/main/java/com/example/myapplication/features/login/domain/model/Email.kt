package com.example.myapplication.features.login.domain.model

@JvmInline
value class Email(val value: String) {
    companion object{
        fun create(raw: String): Email {
            require(raw.isNotEmpty()){
                "Email must not be empty"
            }

            val normalized = raw.trim().lowercase()

            require(normalized.contains("@")){
                "Email must contain @"
            }
            return Email(normalized)
        }
    }
    override fun toString(): String = value
}