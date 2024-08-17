package com.avwaveaf.bitnews.domain.usecase

import com.avwaveaf.bitnews.domain.repository.NewsRepository

class GetNewsHeadlinesUseCase(private val newsRepository: NewsRepository) {
}