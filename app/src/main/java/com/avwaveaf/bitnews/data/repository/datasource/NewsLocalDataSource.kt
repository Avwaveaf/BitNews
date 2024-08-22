package com.avwaveaf.bitnews.data.repository.datasource

import com.avwaveaf.bitnews.data.models.Article
import kotlinx.coroutines.flow.Flow

interface NewsLocalDataSource {
    suspend fun saveArticleToDB(article: Article)
    fun getAllSavedArticle(): Flow<List<Article>>
    suspend fun deleteSavedArticle(article: Article)
}