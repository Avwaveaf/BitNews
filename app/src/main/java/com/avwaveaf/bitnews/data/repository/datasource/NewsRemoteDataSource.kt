package com.avwaveaf.bitnews.data.repository.datasource

import com.avwaveaf.bitnews.data.models.ApiResponse
import retrofit2.Response

interface NewsRemoteDataSource {
    suspend fun getTopHeadlines(country:String, page:Int): Response<ApiResponse>
}