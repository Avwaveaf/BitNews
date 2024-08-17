package com.avwaveaf.bitnews.domain.repository

import com.avwaveaf.bitnews.data.models.ApiResponse
import com.avwaveaf.bitnews.data.models.Article
import com.avwaveaf.bitnews.data.util.Resource
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    // Remote data source
    suspend fun getNewsHeadline(): Resource<ApiResponse>
    suspend fun getSearchedNews(searchQuery: String): Resource<ApiResponse>

    // local data source
    suspend fun saveNews(article: Article)
    suspend fun deleteNews(article: Article)
    fun getSavedNews(): Flow<List<Article>>
}