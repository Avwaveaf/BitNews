package com.avwaveaf.bitnews.di

import android.app.Application
import com.avwaveaf.bitnews.domain.usecase.DeleteSavedNewsUseCase
import com.avwaveaf.bitnews.domain.usecase.GetNewsHeadlinesUseCase
import com.avwaveaf.bitnews.domain.usecase.GetSavedNewsUseCase
import com.avwaveaf.bitnews.domain.usecase.GetSearchedNewsUseCase
import com.avwaveaf.bitnews.domain.usecase.SaveNewsUseCase
import com.avwaveaf.bitnews.presentation.viewmodel.NewsViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FactoryModule {

    @Singleton
    @Provides
    fun provideNewsViewModelFactory(
        app: Application,
        getNewsHeadlinesUseCase: GetNewsHeadlinesUseCase,
        getSearchedNewsUseCase: GetSearchedNewsUseCase,
        saveNewsUseCase: SaveNewsUseCase,
        getSavedNewsUseCase: GetSavedNewsUseCase,
        deleteSavedNewsUseCase: DeleteSavedNewsUseCase
    ): NewsViewModelFactory {
        return NewsViewModelFactory(
            app,
            getNewsHeadlinesUseCase,
            getSearchedNewsUseCase,
            saveNewsUseCase,
            getSavedNewsUseCase,
            deleteSavedNewsUseCase
        )
    }
}