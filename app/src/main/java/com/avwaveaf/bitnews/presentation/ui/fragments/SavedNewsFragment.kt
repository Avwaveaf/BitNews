package com.avwaveaf.bitnews.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.avwaveaf.bitnews.data.models.Article
import com.avwaveaf.bitnews.databinding.FragmentSavedNewsBinding
import com.avwaveaf.bitnews.databinding.NewsListItemBinding
import com.avwaveaf.bitnews.databinding.TopHeadlineNewsItemBinding
import com.avwaveaf.bitnews.presentation.adapter.GenericRecyclerViewAdapter
import com.avwaveaf.bitnews.presentation.ui.activity.MainActivity
import com.avwaveaf.bitnews.presentation.viewmodel.NewsViewModel
import com.google.android.material.snackbar.Snackbar


class SavedNewsFragment : Fragment() {

    private lateinit var newsViewModel: NewsViewModel
    private lateinit var binding: FragmentSavedNewsBinding

    private lateinit var newsAdapter: GenericRecyclerViewAdapter<Article, NewsListItemBinding, TopHeadlineNewsItemBinding>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModelAndAdapter()
        initRecyclerView()
        observeLocalData()
        setupItemTouchHelper(view)
    }

    private fun setupItemTouchHelper(view: View) {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition
                val article = newsAdapter.currentList[pos]
                newsViewModel.deleteSavedArticle(article)
                Snackbar.make(view, "Saved Article Deleted Successfully!", Snackbar.LENGTH_LONG)
                    .apply {
                        setAction("Undo") {
                            newsViewModel.saveNewsArticle(article)
                        }

                        show()
                    }
            }

        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            // attach this callback to our adapter
            attachToRecyclerView(binding.savedNewsRecyclerView)
        }
    }

    private fun observeLocalData() {
        newsViewModel.getAllSavedArticles().observe(viewLifecycleOwner) {
            newsAdapter.submitList(it)
        }
    }

    private fun setupViewModelAndAdapter() {
        newsViewModel = (activity as MainActivity).viewModel
        newsAdapter = (activity as MainActivity).newsAdapter
        // setup each item onClickListener
        newsAdapter.setOnItemClickListener { item ->
            val action =
                SavedNewsFragmentDirections.actionSavedNewsFragmentToNewsDetailFragment(item)
            findNavController().navigate(action)
        }
    }

    private fun initRecyclerView() {
        binding.savedNewsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = newsAdapter
        }
    }
}