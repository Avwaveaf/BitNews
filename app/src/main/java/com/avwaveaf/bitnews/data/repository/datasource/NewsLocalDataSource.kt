package com.avwaveaf.bitnews.data.repository.datasource

import com.avwaveaf.bitnews.data.models.Article

interface NewsLocalDataSource {
    suspend fun saveArticleToDB(article: Article)
}