package com.avwaveaf.bitnews.data.repository

import com.avwaveaf.bitnews.data.models.ApiResponse
import com.avwaveaf.bitnews.data.models.Article
import com.avwaveaf.bitnews.data.repository.datasource.NewsRemoteDataSource
import com.avwaveaf.bitnews.data.util.Resource
import com.avwaveaf.bitnews.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class NewsRepositoryImpl(
    private val newsRemoteDataSource: NewsRemoteDataSource
) : NewsRepository {

    private fun responseToResource(response: Response<ApiResponse>): Resource<ApiResponse> {
        if (response.isSuccessful) {
            response.body()?.let { res -> return Resource.Success(res) }
        }
        return Resource.Error(response.message())
    }

    override suspend fun getNewsHeadline(country: String, page: Int): Resource<ApiResponse> {
        return responseToResource(newsRemoteDataSource.getTopHeadlines(country, page))
    }

    override suspend fun getSearchedNews(searchQuery: String): Resource<ApiResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun saveNews(article: Article) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteNews(article: Article) {
        TODO("Not yet implemented")
    }

    override fun getSavedNews(): Flow<List<Article>> {
        TODO("Not yet implemented")
    }
}