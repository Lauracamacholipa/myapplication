package com.example.myapplication.features.login.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class EmailParametirizedTest (private val inputData: String, private val expected: String,){
    companion object{
        //arrange
        @JvmStatic
        @Parameterized.Parameters(name = "Email.of({0}) = {1}")
        fun data(): Collection<Array<String>> = listOf(
            arrayOf("USER@DOMAIN.COM", "user@domain.com"),
            arrayOf("USER@a.COM", "user@a.com"),
            arrayOf("Foo.Bar@gmail.com", "foo.bar@gmail.com")
        )
    }

    @Test()
    fun `test input data Email`(){
        //act
        val emailValueObject = Email.create(inputData)

        //assert
        assertEquals(expected, emailValueObject.value)
    }
}