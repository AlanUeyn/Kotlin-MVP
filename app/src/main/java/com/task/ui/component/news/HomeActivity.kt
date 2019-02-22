package com.task.ui.component.news

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.test.espresso.IdlingResource
import com.task.R
import com.task.data.remote.dto.NewsItem
import com.task.ui.ViewModelFactory
import com.task.ui.base.BaseActivity
import com.task.ui.base.listeners.RecyclerItemListener
import com.task.ui.component.details.DetailsActivity
import com.task.utils.Constants
import com.task.utils.EspressoIdlingResource
import kotlinx.android.synthetic.main.home_activity.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import javax.inject.Inject

/**
 * Created by AhmedEltaher on 5/12/2016
 */

class HomeActivity : BaseActivity(), HomeContract.View, RecyclerItemListener {
    @Inject
    lateinit var homeViewModel: HomeViewModel
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override val layoutId: Int
        get() = R.layout.home_activity

    val countingIdlingResource: IdlingResource
        @VisibleForTesting
        get() = EspressoIdlingResource.idlingResource

    override fun initializeViewModel() {
        homeViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(HomeViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ic_toolbar_refresh.setOnClickListener {
            incrementCountingIdlingResource()
            showDataIsLoading(true)
            homeViewModel.getNews()
        }
        btn_search.setOnClickListener {
            if (!(et_search.text?.toString().isNullOrEmpty())) {
                setLoaderVisibility(true)
                homeViewModel.onSearchClick(et_search.text?.toString()!!)
            }
        }
        initializeNewsList(homeViewModel)
        homeViewModel.newsSearchFound.observe(this, Observer {newsItem ->
            if(newsItem!=null){
                setLoaderVisibility(false)
                navigateToDetailsScreen(newsItem)
            }else{
                showSearchError()
            }
        })
    }

    override fun initializeNewsList(news: List<NewsItem>) {
        val newsAdapter = NewsAdapter(this, news)
        val layoutManager = LinearLayoutManager(this)
        rv_news_list.layoutManager = layoutManager
        rv_news_list.setHasFixedSize(true)
        rv_news_list.adapter = newsAdapter
    }

    override fun setLoaderVisibility(isVisible: Boolean) {
        pb_loading.visibility = if (isVisible) VISIBLE else GONE
    }

    override fun navigateToDetailsScreen(news: NewsItem) {
        startActivity(intentFor<DetailsActivity>(Constants.NEWS_ITEM_KEY to news))
    }

    override fun setNoDataVisibility(isVisible: Boolean) {
        tv_no_data.visibility = if (isVisible) VISIBLE else GONE
    }

    override fun setListVisibility(isVisible: Boolean) {
        rl_news_list.visibility = if (isVisible) VISIBLE else GONE
    }

    override fun showSearchError() {
        rl_news_list.snackbar(R.string.search_error)
    }

    override fun showNoNewsError() {
        rl_news_list.snackbar(R.string.news_error)
    }

    override fun incrementCountingIdlingResource() {
        EspressoIdlingResource.increment()
    }

    override fun decrementCountingIdlingResource() {
        EspressoIdlingResource.decrement()
    }

    override fun onItemSelected(position: Int) =
            this.navigateToDetailsScreen(news = homeViewModel.newsModel.value?.newsItems?.get(position)!!)

    private fun initializeNewsList(viewModel: HomeViewModel) {
        showDataIsLoading(true)
        viewModel.newsModel.observe(this, Observer { newsModel ->
            // we don't need any null checks here for the adapter since LiveData guarantees that
            // it won't call us if fragment is stopped or not started.
            if (!(newsModel?.newsItems.isNullOrEmpty())) {
                val newsAdapter = NewsAdapter(this, newsModel?.newsItems!!)
                val layoutManager = LinearLayoutManager(this)
                rv_news_list.layoutManager = layoutManager
                rv_news_list.setHasFixedSize(true)
                rv_news_list.adapter = newsAdapter
            } else {
                //TODO NO datax
                toast("some thing went wrong!")
            }
            showDataIsLoading(false)
        })
        viewModel.getNews()
    }

    private fun showDataIsLoading(isLoading: Boolean) {
        if (isLoading) {
            pb_loading.visibility = VISIBLE
            tv_no_data.visibility = GONE
            rl_news_list.visibility = GONE
            incrementCountingIdlingResource()
        } else {
            decrementCountingIdlingResource()
            pb_loading.visibility = GONE
            tv_no_data.visibility = GONE
            rl_news_list.visibility = VISIBLE
        }
    }
}
