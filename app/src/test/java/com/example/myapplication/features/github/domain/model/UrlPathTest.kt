package com.example.myapplication.features.github.domain.model

import org.junit.Test

class UrlPathTest {
    @Test(expected = Exception:: class)
    fun `test UrlPath`( ) {
        UrlPath("mydfdsda")
        UrlPath("")
    }
}