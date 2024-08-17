package com.avwaveaf.bitnews.domain.usecase

import com.avwaveaf.bitnews.domain.repository.NewsRepository

class GetSavedNewsUseCase(private val newsRepository: NewsRepository) {
}