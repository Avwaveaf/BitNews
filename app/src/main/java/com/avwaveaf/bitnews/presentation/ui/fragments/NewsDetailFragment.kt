package com.avwaveaf.bitnews.presentation.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.avwaveaf.bitnews.data.models.Article
import com.avwaveaf.bitnews.databinding.FragmentNewsDetailBinding

class NewsDetailFragment : Fragment() {

    private lateinit var binding: FragmentNewsDetailBinding
    private val args: NewsDetailFragmentArgs by navArgs()
    private val selectedArticle: Article
        get() = args.selectedArticle

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
        setupWebView()
    }

    private fun setupWebView() {
        Log.i("WEBVIEW", "${selectedArticle.title} url: ${selectedArticle.url}")

        // Show loading bar immediately
        binding.loadingProgressBar.visibility = View.VISIBLE

        binding.newsDetailWebView.apply {
            settings.javaScriptEnabled = true
            webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
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

                override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                    super.onReceivedError(view, request, error)
                    binding.loadingProgressBar.visibility = View.GONE
                    binding.loadingTextInfo.visibility = View.GONE
                    binding.newsDetailWebView.visibility = View.GONE
                    // Show an error message
                    binding.errorMessage.visibility = View.VISIBLE
                    binding.errorMessage.text = "Failed to load content. ${error?.description}"
                }

                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
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
