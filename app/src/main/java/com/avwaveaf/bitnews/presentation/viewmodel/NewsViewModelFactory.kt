package com.avwaveaf.bitnews.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.avwaveaf.bitnews.domain.usecase.GetNewsHeadlinesUseCase
import com.avwaveaf.bitnews.domain.usecase.GetSearchedNewsUseCase

@Suppress("UNCHECKED_CAST")
class NewsViewModelFactory(
    private val app: Application,
    private val getNewsHeadlinesUseCase: GetNewsHeadlinesUseCase,
    private val getSearchedNewsUseCase: GetSearchedNewsUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(app, getNewsHeadlinesUseCase, getSearchedNewsUseCase) as T
    }
}