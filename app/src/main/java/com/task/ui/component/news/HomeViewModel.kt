package com.task.ui.component.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.task.data.remote.dto.NewsItem
import com.task.data.remote.dto.NewsModel
import com.task.ui.base.BaseViewModel
import com.task.usecase.NewsUseCase
import javax.inject.Inject

/**
 * Created by AhmedEltaher on 5/12/2016
 */

class HomeViewModel @Inject
constructor(val newsUseCase: NewsUseCase) : BaseViewModel(), HomeContract.IViewModel {
    var newsModel: LiveData<NewsModel> = MutableLiveData()
    var newsSearchFound: MutableLiveData<NewsItem> = MutableLiveData()
    var noSearchFound: MutableLiveData<Boolean> = MutableLiveData()

     override fun getNews() {
        newsModel = newsUseCase.getNewsAsync()!!
    }

    override fun onSearchClick(newsTitle: String) {
        val news = newsModel?.value?.newsItems
        if (!newsTitle.isEmpty() && !news.isNullOrEmpty()) {
            newsSearchFound.value = newsUseCase.searchByTitle(news, newsTitle)
        } else {
            noSearchFound.value=true
        }
    }
}
