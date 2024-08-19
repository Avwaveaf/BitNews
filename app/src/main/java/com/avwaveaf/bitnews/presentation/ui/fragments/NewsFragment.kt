package com.avwaveaf.bitnews.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.avwaveaf.bitnews.data.models.Article
import com.avwaveaf.bitnews.data.util.Resource
import com.avwaveaf.bitnews.databinding.FragmentNewsBinding
import com.avwaveaf.bitnews.databinding.NewsListItemBinding
import com.avwaveaf.bitnews.databinding.TopHeadlineNewsItemBinding
import com.avwaveaf.bitnews.presentation.adapter.GenericRecyclerViewAdapter
import com.avwaveaf.bitnews.presentation.ui.activity.MainActivity
import com.avwaveaf.bitnews.presentation.viewmodel.NewsViewModel
import kotlinx.coroutines.launch


class NewsFragment : Fragment() {

    private lateinit var newsViewModel: NewsViewModel
    private lateinit var binding: FragmentNewsBinding
    private lateinit var newsAdapter: GenericRecyclerViewAdapter<Article, NewsListItemBinding, TopHeadlineNewsItemBinding>

    private var countryCode = "us"
    private var currentPage = 1
    private var isLoading = false
    private var isLastPage = false
    private var totalPages = 1

    companion object {
        private const val BUNDLE_KEY_SELECTED_ARTICLE = "SELECTED_ARTICLE"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModelAndAdapter()
        initRecyclerView()
        observeNewsHeadlines()
        fetchNewsList()
    }

    private fun setupViewModelAndAdapter() {
        newsViewModel = (activity as MainActivity).viewModel
        newsAdapter = (activity as MainActivity).newsAdapter
        newsAdapter.setOnItemClickListener { item ->
            val action = NewsFragmentDirections.actionNewsFragmentToNewsDetailFragment(item)
            findNavController().navigate(action)
        }
    }

    private fun initRecyclerView() {
        binding.newsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = newsAdapter
            addOnScrollListener(createScrollListener())
        }
    }

    private fun createScrollListener(): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0
                        && totalItemCount >= 20
                    ) {
                        loadMoreItems()
                    }
                }
            }
        }
    }

    private fun loadMoreItems() {
        if (currentPage < totalPages) {
            currentPage++
            fetchNewsList()
        }
    }

    private fun fetchNewsList() {
        if (isLoading) return

        isLoading = true
        showProgressBar()
        lifecycleScope.launch {
            newsViewModel.getNewsHeadline(countryCode, currentPage)
        }
    }

    private fun observeNewsHeadlines() {
        newsViewModel.newsHeadlines.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        val newItems = newsResponse.articles
                        if (currentPage == 1) {
                            newsAdapter.submitList(newItems)
                        } else {
                            val currentList = ArrayList(newsAdapter.currentList)
                            currentList.addAll(newItems)
                            newsAdapter.submitList(currentList)
                        }
                        totalPages = (newsResponse.totalResults + 19) / 20 // Round up division
                        isLastPage = currentPage == totalPages
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { errorMessage ->
                        Toast.makeText(activity, "Error: $errorMessage", Toast.LENGTH_LONG).show()
                    }
                }

                is Resource.Loading -> {
                    // Progress bar visibility is handled in fetchNewsList
                }
            }
            isLoading = false
        }
    }

    private fun showProgressBar() {
        binding.newsProgressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.newsProgressBar.visibility = View.GONE
    }
}