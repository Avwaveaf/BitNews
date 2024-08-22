package com.avwaveaf.bitnews.presentation.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.avwaveaf.bitnews.R
import com.avwaveaf.bitnews.data.models.Article
import com.avwaveaf.bitnews.databinding.ActivityMainBinding
import com.avwaveaf.bitnews.databinding.NewsListItemBinding
import com.avwaveaf.bitnews.databinding.TopHeadlineNewsItemBinding
import com.avwaveaf.bitnews.presentation.adapter.GenericRecyclerViewAdapter
import com.avwaveaf.bitnews.presentation.viewmodel.NewsViewModel
import com.avwaveaf.bitnews.presentation.viewmodel.NewsViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var factory: NewsViewModelFactory

    lateinit var viewModel: NewsViewModel

    @Inject
    lateinit var newsAdapter: GenericRecyclerViewAdapter<Article, NewsListItemBinding, TopHeadlineNewsItemBinding>

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupBottomNavigation()
        setupViewModel()
        setupEdgeToEdge()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, factory)[NewsViewModel::class.java]
    }

    private fun setupEdgeToEdge() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupBottomNavigation() {
        try {
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
            val navController = navHostFragment.navController
            binding.bottomNavigationView.setupWithNavController(navController)
            Log.d(TAG, "Bottom navigation setup completed")
        } catch (e: Exception) {
            Log.e(TAG, "Error setting up bottom navigation", e)
        }
    }
}