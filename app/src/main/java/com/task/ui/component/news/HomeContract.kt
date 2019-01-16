package com.task.ui.component.news

import com.task.data.remote.dto.NewsItem
import com.task.ui.base.listeners.BaseView

/**
 * Created by AhmedEltaher on 5/12/2016
 */

interface HomeContract {

    interface View : BaseView {
        fun initializeNewsList(news: List<NewsItem>)

        fun setLoaderVisibility(isVisible: Boolean)

        fun navigateToDetailsScreen(news: NewsItem)

        fun setNoDataVisibility(isVisible: Boolean)

        fun setListVisibility(isVisible: Boolean)

        fun showSearchError()

        fun showNoNewsError()

        fun incrementCountingIdlingResource()

        fun decrementCountingIdlingResource()
    }


    interface IViewModel {

        fun getNews()

        fun onSearchClick(newsTitle: String)

    }
}
