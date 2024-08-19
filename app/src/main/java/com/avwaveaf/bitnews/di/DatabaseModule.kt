package com.avwaveaf.bitnews.di

import android.app.Application
import androidx.room.Room
import com.avwaveaf.bitnews.data.db.ArticleDAO
import com.avwaveaf.bitnews.data.db.ArticleDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDBInstance(app: Application): ArticleDB {
        return Room.databaseBuilder(app, ArticleDB::class.java, "news_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideArticleDao(articleDB: ArticleDB): ArticleDAO {
        return articleDB.getArticleDao()
    }
}