package com.avwaveaf.bitnews.domain.usecase

import com.avwaveaf.bitnews.domain.repository.NewsRepository

class DeleteSavedNewsUseCase(private val newsRepository: NewsRepository) {
}