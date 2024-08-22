package com.avwaveaf.bitnews.presentation.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.avwaveaf.bitnews.R
import com.avwaveaf.bitnews.data.models.Article
import com.avwaveaf.bitnews.databinding.FragmentNewsDetailBinding
import com.avwaveaf.bitnews.presentation.ui.activity.MainActivity
import com.avwaveaf.bitnews.presentation.viewmodel.NewsViewModel
import com.google.android.material.snackbar.Snackbar

class NewsDetailFragment : Fragment() {

    private lateinit var binding: FragmentNewsDetailBinding
    private val args: NewsDetailFragmentArgs by navArgs()
    private val selectedArticle: Article
        get() = args.selectedArticle

    private var isFabExpanding = false

    private lateinit var newsViewModel: NewsViewModel


    private val emergeFromBottomAnim: Animation by lazy {
        AnimationUtils.loadAnimation(context, R.anim.emerge_from_bottom)
    }

    private val emergeToBottomAnim: Animation by lazy {
        AnimationUtils.loadAnimation(context, R.anim.emerge_to_bottom)
    }

    private val rotateClockwiseAnim: Animation by lazy {
        AnimationUtils.loadAnimation(context, R.anim.rotate_clockwise)
    }

    private val rotateCounterClockwiseAnim: Animation by lazy {
        AnimationUtils.loadAnimation(context, R.anim.rotate_counter_clockwise)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentNewsDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupWebView()
        fabSetup()
        setupOverlayBgFab()
        setupFabOnClickListener(view)
    }

    private fun setupViewModel() {
        newsViewModel = (activity as MainActivity).viewModel
    }

    private fun setupOverlayBgFab() {
        binding.overlayView.setOnClickListener {
            if (isFabExpanding) {
                minifyFab()
            }
        }
    }


    private fun fabSetup() {
        binding.triggerMenuOpenFab.setOnClickListener {
            if (isFabExpanding) {
                minifyFab()
            } else {
                expandFab()
            }
        }
    }

    private fun expandFab() {
        // Set the FABs and texts to visible before starting the animation
        binding.saveFab.visibility = View.VISIBLE
        binding.saveFabHintTv.visibility = View.VISIBLE
        binding.shareFab.visibility = View.VISIBLE
        binding.shareFabHintTv.visibility = View.VISIBLE

        // Show overlay to detect clicks outside the FAB menu
        binding.overlayView.visibility = View.VISIBLE

        // Rotate the main FAB
        binding.triggerMenuOpenFab.startAnimation(rotateClockwiseAnim)

        // Emerge the FAB along with its text
        binding.saveFab.startAnimation(emergeFromBottomAnim)
        binding.saveFabHintTv.startAnimation(emergeFromBottomAnim)
        binding.shareFab.startAnimation(emergeFromBottomAnim)
        binding.shareFabHintTv.startAnimation(emergeFromBottomAnim)



        isFabExpanding = true

    }

    private fun setupFabOnClickListener(view: View) {
        binding.saveFab.setOnClickListener {
            newsViewModel.saveNewsArticle(selectedArticle)
            Snackbar.make(view, "Article saved !", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun minifyFab() {
        // Rotate the main FAB
        binding.triggerMenuOpenFab.startAnimation(rotateCounterClockwiseAnim)

        // Collapse the FAB
        binding.saveFab.startAnimation(emergeToBottomAnim)
        binding.saveFabHintTv.startAnimation(emergeToBottomAnim)
        binding.shareFab.startAnimation(emergeToBottomAnim)
        binding.shareFabHintTv.startAnimation(emergeToBottomAnim)

        // Hide the overlay
        binding.overlayView.visibility = View.GONE

        // Set the FABs and texts to GONE after the animation ends
        emergeToBottomAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                binding.saveFab.visibility = View.GONE
                binding.saveFabHintTv.visibility = View.GONE
                binding.shareFab.visibility = View.GONE
                binding.shareFabHintTv.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })

        isFabExpanding = false
    }


    private fun setupWebView() {
        Log.i("WEBVIEW", "${selectedArticle.title} url: ${selectedArticle.url}")

        // Show loading bar immediately
        binding.loadingProgressBar.visibility = View.VISIBLE

        binding.newsDetailWebView.apply {
            settings.javaScriptEnabled = true
            webViewClient = object : WebViewClient() {
                override fun onPageStarted(
                    view: WebView?,
                    url: String?,
                    favicon: android.graphics.Bitmap?
                ) {
                    super.onPageStarted(view, url, favicon)
                    binding.loadingProgressBar.visibility = View.VISIBLE
                    binding.loadingTextInfo.visibility = View.VISIBLE
                    binding.newsDetailWebView.visibility = View.GONE
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    binding.loadingProgressBar.visibility = View.GONE
                    binding.loadingTextInfo.visibility = View.GONE
                    binding.newsDetailWebView.visibility = View.VISIBLE
                }

                override fun onReceivedError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    error: WebResourceError?
                ) {
                    super.onReceivedError(view, request, error)
                    binding.loadingProgressBar.visibility = View.GONE
                    binding.loadingTextInfo.visibility = View.GONE
                    binding.newsDetailWebView.visibility = View.GONE
                    // Show an error message
                    binding.errorMessage.visibility = View.VISIBLE
                    binding.errorMessage.text = "Failed to load content. ${error?.description}"
                }

                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    // This ensures that all URLs are loaded within the WebView
                    request?.url?.let { view?.loadUrl(it.toString()) }
                    return true
                }
            }

            // Load the URL after setting up the WebViewClient
            selectedArticle.url?.let { url ->
                if (url.isNotBlank()) {
                    loadUrl(url)
                } else {
                    // Handle case where URL is blank
                    binding.loadingProgressBar.visibility = View.GONE
                    binding.loadingTextInfo.visibility = View.GONE
                    binding.newsDetailWebView.visibility = View.GONE
                    binding.errorMessage.visibility = View.VISIBLE
                    binding.errorMessage.text = "No URL provided for this article."
                }
            } ?: run {
                // Handle case where URL is null
                binding.loadingProgressBar.visibility = View.GONE
                binding.loadingTextInfo.visibility = View.GONE
                binding.newsDetailWebView.visibility = View.GONE
                binding.errorMessage.visibility = View.VISIBLE
                binding.errorMessage.text = "No URL available for this article."
            }
        }
    }


}
