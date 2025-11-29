package com.example.myapplication.features.login.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

class EmailTest {
    @Test()
    fun `test input lowercase data Email`(){
        //arrange
        val inputData = "AdrIan@gmail.COM"
        val expected = "adrian@gmail.com"

        //act
        val emailValueObject = Email.create(inputData)

        //assert
        assertEquals(expected, emailValueObject.value)
    }
}