package com.avwaveaf.bitnews.domain.usecase

import com.avwaveaf.bitnews.data.models.ApiResponse
import com.avwaveaf.bitnews.data.util.Resource
import com.avwaveaf.bitnews.domain.repository.NewsRepository

class GetNewsHeadlinesUseCase(private val newsRepository: NewsRepository) {
    suspend fun execute(): Resource<ApiResponse> {
        return newsRepository.getNewsHeadline()
    }
}