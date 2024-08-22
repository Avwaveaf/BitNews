package com.avwaveaf.bitnews.presentation.ui.fragments


import CarouselAdapter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
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

    // SETUP CAROUSEL
    private val sliderHandler = Handler(Looper.getMainLooper())
    private val sliderRunnable = kotlinx.coroutines.Runnable {
        binding.carouselViewPager.currentItem =
            (binding.carouselViewPager.currentItem + 1) % binding.carouselViewPager.adapter!!.itemCount
    }


    private var carouselItems: List<Article> = emptyList()

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
        observeSearchedNews()
        fetchNewsList()
        setupSearchView()
    }

    private fun setupSlider() {
        if (carouselItems.isNotEmpty()) {
            val carouselAdapter = CarouselAdapter(carouselItems)
            binding.carouselViewPager.adapter = carouselAdapter
            startAutoSlide()
        }
    }

    private fun startAutoSlide() {
        sliderHandler.postDelayed(sliderRunnable, 5000)
    }
    private fun stopAutoSlide() {
        sliderHandler.removeCallbacks(sliderRunnable)
    }
    override fun onResume() {
        super.onResume()
        if (carouselItems.isNotEmpty()) {
            startAutoSlide()
        }
    }

    override fun onPause() {
        super.onPause()
        stopAutoSlide()
    }

    private fun setupSearchView() {
        binding.apply {
            searchView.editText.setOnEditorActionListener { textView, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val query = textView.text.toString()
                    if (query.isNotEmpty()) {
                        searchView.hide()
                        searchBar.setText(query)
                        newsAdapter.submitList(null) // Clear previous results
                        fetchSearchedNews(query)
                    } else {
                        // Clear search and return to default news list
                        searchBar.setText("")
                        newsAdapter.submitList(null)
                        currentPage = 1
                        fetchNewsList()
                    }
                    true
                } else {
                    false
                }
            }
        }
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
            if (binding.searchBar.text.isEmpty()) {
                fetchNewsList()
            } else {
                fetchSearchedNews(binding.searchBar.text.toString())
            }
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

    private fun fetchSearchedNews(searchQuery: String) {
        if (isLoading) return

        isLoading = true
        showProgressBar()
        currentPage = 1 // Reset to first page
        isLastPage = false // Reset last page flag
        lifecycleScope.launch {
            newsViewModel.searchNews(searchQuery, currentPage)
        }
    }

    private fun observeSearchedNews() {
        newsViewModel.searchedNews.observe(viewLifecycleOwner) { response ->
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
                    showProgressBar()
                }
            }
            isLoading = false
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
                            // take 5 elements from new article
                            carouselItems = newItems.take(5)
                            setupSlider()
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
                    showProgressBar()
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