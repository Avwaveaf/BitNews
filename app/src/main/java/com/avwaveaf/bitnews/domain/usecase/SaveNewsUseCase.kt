package com.avwaveaf.bitnews.domain.usecase

import com.avwaveaf.bitnews.data.models.Article
import com.avwaveaf.bitnews.domain.repository.NewsRepository

class SaveNewsUseCase(private val newsRepository: NewsRepository) {
    suspend fun execute(article: Article) = newsRepository.saveNews(article)
}