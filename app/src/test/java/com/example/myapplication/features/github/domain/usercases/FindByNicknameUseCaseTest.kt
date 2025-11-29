package com.example.myapplication.features.github.domain.usercases

import com.example.myapplication.features.github.domain.model.NickName
import com.example.myapplication.features.github.domain.model.UrlPath
import com.example.myapplication.features.github.domain.model.UserModel
import com.example.myapplication.features.github.domain.repository.IgithubRepository
import io.mockk.coEvery
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FindByNicknameUseCaseTest {
    private val repository = mockk<IgithubRepository>()
    private val useCase = FindByNickNameUseCase(repository)

    @Test
    fun `should return success when repository returns user`() = runTest{
        //arrange
        val expected = UserModel(NickName("juan"), UrlPath("https://test.url"))
        coEvery { repository.findbyNickName("juan") } returns Result.success(expected)

        //act
        val result = useCase.invoke("juan")

        //assert
        assert(result.isSuccess)
        assertEquals(expected, result.getOrNull())
    }

    @Test
    fun `should return failure when repository returns error`() = runTest {
        //arrange
        val exception = Exception("User not found")
        coEvery { repository.findbyNickName("nonexistent") } returns Result.failure(exception)

        //act
        val result = useCase.invoke("nonexistent")

        //assert
        assert(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}