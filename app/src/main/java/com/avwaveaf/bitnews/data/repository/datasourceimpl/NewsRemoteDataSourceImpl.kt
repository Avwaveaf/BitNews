package com.avwaveaf.bitnews.data.repository.datasourceimpl

import com.avwaveaf.bitnews.data.api.NewsApiService
import com.avwaveaf.bitnews.data.models.ApiResponse
import com.avwaveaf.bitnews.data.repository.datasource.NewsRemoteDataSource
import retrofit2.Response

class NewsRemoteDataSourceImpl(
    private val newsApiService: NewsApiService,
) : NewsRemoteDataSource {
    override suspend fun getTopHeadlines(country: String, page: Int): Response<ApiResponse> {
        return newsApiService.getTopHeadlines(country, page)
    }

    override suspend fun getSearchedNews(searchQuery: String, page: Int): Response<ApiResponse> {
        return newsApiService.getSearchedNews(searchQuery, page = page)
    }

}