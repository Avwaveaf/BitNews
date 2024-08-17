package com.avwaveaf.bitnews.domain.usecase

import com.avwaveaf.bitnews.domain.repository.NewsRepository

class SaveNewsUseCase(private val newsRepository: NewsRepository) {
}