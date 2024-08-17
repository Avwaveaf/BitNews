package com.avwaveaf.bitnews.di

import com.avwaveaf.bitnews.data.repository.NewsRepositoryImpl
import com.avwaveaf.bitnews.data.repository.datasource.NewsRemoteDataSource
import com.avwaveaf.bitnews.domain.repository.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideNewsRepository(newsRemoteDataSource: NewsRemoteDataSource): NewsRepository {
        return NewsRepositoryImpl(newsRemoteDataSource)
    }
}