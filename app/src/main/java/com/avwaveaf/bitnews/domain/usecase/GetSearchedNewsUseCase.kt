package com.avwaveaf.bitnews.domain.usecase

import com.avwaveaf.bitnews.data.models.ApiResponse
import com.avwaveaf.bitnews.data.util.Resource
import com.avwaveaf.bitnews.domain.repository.NewsRepository

class GetSearchedNewsUseCase(private val newsRepository: NewsRepository) {
    suspend fun execute(searchQuery: String): Resource<ApiResponse> {
        return newsRepository.getSearchedNews(searchQuery)
    }
}