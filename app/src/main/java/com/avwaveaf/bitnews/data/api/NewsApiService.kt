package com.avwaveaf.bitnews.data.api

import com.avwaveaf.bitnews.data.models.ApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

interface NewsApiService {
    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("country")
        country: String,

        @Query("page")
        page: Int,

        @Query("apiKey")
        apiKey: String = "afd9079834af460f97174745c40a18df"
    ): Response<ApiResponse>

    @GET("everything")
    suspend fun getSearchedNews(
        @Query("q")
        searchQuery: String,

        @Query("searchIn")
        searchInField: String = "title,content,description",

        @Query("sortBy")
        sortBy: String = "popularity",

        @Query("from")
        dateFrom: String = getLastMonthDate(),

        @Query("to")
        dateTo: String = getCurrentDate(),

        @Query("page")
        page: Int,

        @Query("apiKey")
        apiKey: String = "afd9079834af460f97174745c40a18df"
    ): Response<ApiResponse>

    companion object {
        private fun getCurrentDate(): String {
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            return dateFormat.format(calendar.time)
        }

        private fun getLastMonthDate(): String {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.MONTH, -1)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            return dateFormat.format(calendar.time)
        }
    }
}
