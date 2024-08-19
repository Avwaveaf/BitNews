package com.avwaveaf.bitnews.presentation.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.core.content.getSystemService
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.avwaveaf.bitnews.data.models.ApiResponse
import com.avwaveaf.bitnews.data.models.Article
import com.avwaveaf.bitnews.data.util.Resource
import com.avwaveaf.bitnews.domain.usecase.GetNewsHeadlinesUseCase
import com.avwaveaf.bitnews.domain.usecase.GetSearchedNewsUseCase
import com.avwaveaf.bitnews.domain.usecase.SaveNewsUseCase
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class NewsViewModel(
    private val app: Application,
    val getNewsHeadlinesUseCase: GetNewsHeadlinesUseCase,
    val getSearchedNewsUseCase: GetSearchedNewsUseCase,
    val saveNewsUseCase: SaveNewsUseCase
) : AndroidViewModel(app) {
    val newsHeadlines: MutableLiveData<Resource<ApiResponse>> = MutableLiveData()
    fun getNewsHeadline(countryCode: String, page: Int) = viewModelScope.launch(IO) {
        // setup loading state resource first
        newsHeadlines.postValue(Resource.Loading())

        try {
            if (isInternetAvailable(app)) {
                val response = getNewsHeadlinesUseCase.execute(countryCode, page)
                newsHeadlines.postValue(response)
            } else {
                newsHeadlines.postValue(Resource.Error("Internet Not Available!"))
            }
        } catch (e: Exception) {
            newsHeadlines.postValue(Resource.Error(e.message.toString()))
        }

    }


    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService<ConnectivityManager>()
        if (connectivityManager != null) {
            val networkCapabilities = connectivityManager.activeNetwork?.let { network ->
                connectivityManager.getNetworkCapabilities(network)
            }
            return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        }
        return false
    }

    // SEARCHED NEWS VIEW MODEL
    val searchedNews: MutableLiveData<Resource<ApiResponse>> = MutableLiveData()

    fun searchNews(searchQuery: String, page: Int) = viewModelScope.launch(IO) {
        // set beginning loading resource state
        searchedNews.postValue(Resource.Loading())

        try {
            if (isInternetAvailable(app)) {
                val response = getSearchedNewsUseCase.execute(searchQuery, page)
                if (response.data == null || response.data.totalResults == 0) {
                    searchedNews.postValue(Resource.Error("No Data Available!"))
                } else {
                    searchedNews.postValue(response)
                }
            } else {
//            set data state to error
                searchedNews.postValue(Resource.Error("No Internet Connection Available!"))
            }
        } catch (e: Exception) {
            searchedNews.postValue(Resource.Error("Error Fetching data: ${e.message.toString()}"))
        }


    }

    // LOCAL DATA SOURCE IMPL
    fun saveNewsArticle(article: Article) = viewModelScope.launch(IO) {
        saveNewsUseCase.execute(article)
    }

}