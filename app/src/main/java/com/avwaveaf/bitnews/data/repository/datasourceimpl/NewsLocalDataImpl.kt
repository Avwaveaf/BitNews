package com.avwaveaf.bitnews.data.repository.datasourceimpl

import com.avwaveaf.bitnews.data.db.ArticleDAO
import com.avwaveaf.bitnews.data.models.Article
import com.avwaveaf.bitnews.data.repository.datasource.NewsLocalDataSource
import kotlinx.coroutines.flow.Flow

class NewsLocalDataImpl(private val articleDAO: ArticleDAO) : NewsLocalDataSource {
    override suspend fun saveArticleToDB(article: Article) {
        articleDAO.insertArticle(article)
    }

    override fun getAllSavedArticle(): Flow<List<Article>> {
        return articleDAO.getAllSavedArticle()
    }

    override suspend fun deleteSavedArticle(article: Article) {
        return articleDAO.deleteSavedArticle(
            article
        )
    }
}