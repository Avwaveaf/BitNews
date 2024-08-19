package com.avwaveaf.bitnews.di

import com.avwaveaf.bitnews.R
import com.avwaveaf.bitnews.data.models.Article
import com.avwaveaf.bitnews.databinding.NewsListItemBinding
import com.avwaveaf.bitnews.databinding.TopHeadlineNewsItemBinding
import com.avwaveaf.bitnews.presentation.adapter.GenericRecyclerViewAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class GenericAdapterModule {

    @Singleton
    @Provides
    fun provideGenericListAdapter(): GenericRecyclerViewAdapter<Article, NewsListItemBinding, TopHeadlineNewsItemBinding> {
        return GenericRecyclerViewAdapter(
            inflatePrimary = { inflater, parent, attachToParent ->
                NewsListItemBinding.inflate(inflater, parent, attachToParent)
            },
            inflateAlt = { inflater, parent, attachToParent ->
                TopHeadlineNewsItemBinding.inflate(inflater, parent, attachToParent)
            },
            bindPrimary = { binding, newsItem ->
                // Bind data to primary layout views
                binding.newsItemTitle.text = newsItem.title
                binding.newsItemPublishedDate.text = newsItem.publishedAt
                binding.newsAuthor.text = newsItem.author
                // image processing with fallback
                Glide.with(binding.newsItemPhoto.context)
                    .load(newsItem.urlToImage)
                    .apply(
                        RequestOptions()
                            .placeholder(R.drawable.absurd_design___chapter_1___34) // Placeholder while loading
                            .error(R.drawable.absurd_design___chapter_1___34) // Error image if loading fails
                    )
                    .into(binding.newsItemPhoto)
            },
            bindAlt = { binding, newsItem ->
                // Bind data to alternate layout views
                binding.headlineTitle.text = newsItem.title
                binding.headlineAuthor.text = newsItem.author
                binding.headlinePublishedDate.text = newsItem.publishedAt
                // image processing with fallback
                Glide.with(binding.headlinePicture.context)
                    .load(newsItem.urlToImage)
                    .apply(
                        RequestOptions()
                            .placeholder(R.drawable.absurd_design___chapter_1___34) // Placeholder while loading
                            .error(R.drawable.absurd_design___chapter_1___34) // Error image if loading fails
                    )
                    .into(binding.headlinePicture)
            },
            areItemsTheSameCondition = { oldItem, newItem ->
                oldItem.url == newItem.url
            },
            areContentsTheSameCondition = { oldItem, newItem ->
                oldItem == newItem
            }
        )
    }
}