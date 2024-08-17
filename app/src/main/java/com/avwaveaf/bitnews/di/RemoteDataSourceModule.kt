package com.avwaveaf.bitnews.di

import com.avwaveaf.bitnews.data.api.NewsApiService
import com.avwaveaf.bitnews.data.repository.datasource.NewsRemoteDataSource
import com.avwaveaf.bitnews.data.repository.datasourceimpl.NewsRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RemoteDataSourceModule {
    @Provides
    @Singleton
    fun provideNewsRemoteDataSource(newsApiService: NewsApiService): NewsRemoteDataSource {
        return NewsRemoteDataSourceImpl(newsApiService)
    }
}