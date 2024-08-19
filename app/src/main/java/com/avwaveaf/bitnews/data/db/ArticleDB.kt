package com.avwaveaf.bitnews.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.avwaveaf.bitnews.data.models.Article

@Database(entities = [Article::class], version = 1, exportSchema = false)
@TypeConverters(TypeConverter::class)
abstract class ArticleDB : RoomDatabase() {
    abstract fun getArticleDao(): ArticleDAO
}