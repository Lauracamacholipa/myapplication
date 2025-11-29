package com.example.myapplication.features.movies.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import com.example.myapplication.features.dollar.data.database.dao.IDollarDao
import com.example.myapplication.features.dollar.data.database.entity.DollarEntity
import com.example.myapplication.features.movies.data.database.dao.IMovieDao
import com.example.myapplication.features.movies.data.database.entity.MovieEntity

/*val MIGRATION_1_2 = object: Migration(2,3){
    override fun migrate(database: SQLiteConnection) {
        database.execSQL("ALTER TABLE 'movies' ADD COLUMN timestamp TEXT")
        database.execSQL("UPDATE movies SET timestamp = '' WHERE timestamp IS NULL")
    }
}*/

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SQLiteConnection) {
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS `movies` (
                `id` INTEGER PRIMARY KEY NOT NULL, 
                `title` TEXT NOT NULL, 
                `poster_url` TEXT NOT NULL, 
                `overview` TEXT NOT NULL
            )
        """.trimIndent())
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SQLiteConnection) {
        database.execSQL("ALTER TABLE `movies` ADD COLUMN `timestamp` TEXT")
        database.execSQL("UPDATE movies SET timestamp = '' WHERE timestamp IS NULL")
    }
}

// Migración de 3 a 4: Agregar columna favourite
val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(connection: SQLiteConnection) {
        connection.execSQL("ALTER TABLE 'movies' ADD COLUMN favourite INTEGER NOT NULL DEFAULT 0")
    }
}

@Database(entities = [DollarEntity::class, MovieEntity::class], version = 4)
abstract class AppRoomDatabase: RoomDatabase() {
    abstract fun dollarDao(): IDollarDao
    abstract fun movieDao(): IMovieDao


    companion object {
        @Volatile
        private var Instance: AppRoomDatabase? = null

        /*fun getDatabase(context: Context): AppRoomDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppRoomDatabase::class.java,
                    "movies_db"
                )
                    //.fallbackToDestructiveMigration()
                    .addMigrations(MIGRATION_1_2)
                    .build()
                    .also { Instance = it }
            }
        }*/


        fun getDatabase(context: Context): AppRoomDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    AppRoomDatabase::class.java,
                    "local_db")
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}