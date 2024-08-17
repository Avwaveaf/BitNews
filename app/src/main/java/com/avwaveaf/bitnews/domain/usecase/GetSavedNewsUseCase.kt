package com.avwaveaf.bitnews.domain.usecase

import com.avwaveaf.bitnews.data.models.Article
import com.avwaveaf.bitnews.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow

class GetSavedNewsUseCase(private val newsRepository: NewsRepository) {
    fun execute(): Flow<List<Article>> {
        return newsRepository.getSavedNews()
    }
}