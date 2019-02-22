package com.task.ui.component.news

import androidx.lifecycle.MutableLiveData
import com.task.data.remote.dto.NewsItem
import com.task.data.remote.dto.NewsModel
import com.task.ui.base.BaseViewModel
import com.task.ui.base.listeners.BaseCallback
import com.task.usecase.NewsUseCase
import javax.inject.Inject

/**
 * Created by AhmedEltaher on 5/12/2016
 */

class HomeViewModel @Inject
constructor(newsDataUseCase: NewsUseCase) : BaseViewModel(), HomeContract.IViewModel {

    var newsUseCase: NewsUseCase = newsDataUseCase
    var newsModel: MutableLiveData<NewsModel> = MutableLiveData()
    var newsSearchFound: MutableLiveData<NewsItem> = MutableLiveData()
    var noSearchFound: MutableLiveData<Boolean> = MutableLiveData()

    override fun getNews() {
        newsUseCase.getNews(callback)
    }

    val callback = object : BaseCallback {
        override fun onSuccess(newsModelData: NewsModel) {
            newsModel.postValue(newsModelData)
        }

        override fun onFail() {
            newsModel.postValue(null)
        }
    }

    override fun onSearchClick(newsTitle: String) {
        val news = newsModel.value?.newsItems
        if (!newsTitle.isEmpty() && !news.isNullOrEmpty()) {
            newsSearchFound.value = newsUseCase.searchByTitle(news, newsTitle)
        } else {
            noSearchFound.value = true
        }
    }
}
