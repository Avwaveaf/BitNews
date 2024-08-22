package com.avwaveaf.bitnews.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.avwaveaf.bitnews.data.models.Article
import kotlinx.coroutines.flow.Flow


@Dao
interface ArticleDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: Article)

    @Query("SELECT * FROM articles")
    fun getAllSavedArticle(): Flow<List<Article>>

    @Delete
    fun deleteSavedArticle(article: Article)

}