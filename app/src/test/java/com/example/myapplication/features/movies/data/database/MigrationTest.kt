package com.example.myapplication.features.movies.data.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myapplication.features.dollar.data.database.entity.DollarEntity
import com.example.myapplication.features.movies.data.database.entity.MovieEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class DatabaseIntegrityTest {
    private lateinit var db: AppRoomDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            AppRoomDatabase::class.java
        ).build()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun testInsertAndReadDollar() = runBlocking {
        val dollar = DollarEntity(
            id = 0,
            dollarOfficial = "6.96",
            dollarParalelo = "7.50",
            usdt = "7.00",
            usdc = "7.01",
            timestamp = System.currentTimeMillis()
        )

        db.dollarDao().insert(dollar)
        val dollars = db.dollarDao().getList()

        assertEquals(1, dollars.size)
        assertEquals("6.96", dollars[0].dollarOfficial)
    }

    @Test
    @Throws(Exception::class)
    fun testInsertAndReadMovie() = runBlocking {
        val movie = MovieEntity(
            id = 1,
            title = "Test Movie",
            posterUrl = "http://test.com/poster.jpg",
            overview = "Test overview",
            timestamp = ""
        )

        db.movieDao().insertMovies(listOf(movie))
        val movies = db.movieDao().getAllMovies().first()

        assertEquals(1, movies.size)
        assertEquals("Test Movie", movies[0].title)
    }

    @Test
    @Throws(Exception::class)
    fun testMovieWithTimestamp() = runBlocking {
        val movie = MovieEntity(
            id = 1,
            title = "Test Movie",
            posterUrl = "http://test.com/poster.jpg",
            overview = "Test overview",
            timestamp = "2024-01-01"
        )

        db.movieDao().insertMovies(listOf(movie))
        val movies = db.movieDao().getAllMovies().first()

        assertEquals("2024-01-01", movies[0].timestamp)
    }
}