package com.example.myapplication.features.profile.domain.model

import com.example.myapplication.features.login.domain.model.Email
import org.junit.Assert.assertEquals
import org.junit.Test

class EmailProfileTest {
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