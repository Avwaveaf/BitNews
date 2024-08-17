package com.avwaveaf.bitnews.domain.usecase

import com.avwaveaf.bitnews.domain.repository.NewsRepository

class GetSearchedNewsUseCase(private val newsRepository: NewsRepository) {
}