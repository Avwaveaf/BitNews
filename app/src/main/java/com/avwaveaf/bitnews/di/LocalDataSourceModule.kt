package com.avwaveaf.bitnews.di

import com.avwaveaf.bitnews.data.db.ArticleDAO
import com.avwaveaf.bitnews.data.repository.datasource.NewsLocalDataSource
import com.avwaveaf.bitnews.data.repository.datasourceimpl.NewsLocalDataImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalDataSourceModule {

    @Singleton
    @Provides
    fun provideLocalDataSource(articleDAO: ArticleDAO): NewsLocalDataSource {
        return NewsLocalDataImpl(articleDAO)
    }
}