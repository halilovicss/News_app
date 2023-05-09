package com.appvision.newsapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.appvision.newsapp.data.model.ArticleModel

@Database(entities = [ArticleModel::class], version = 1, exportSchema = false)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun getDAO(): DAO

    companion object {
        @Volatile
        private var INSTANCE: LocalDatabase? = null
        fun getDatabaseData(context: Context): LocalDatabase {
            return INSTANCE ?: synchronized(LocalDatabase::class) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, LocalDatabase::class.java, "database"
                ).allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                instance

            }
        }
    }
}