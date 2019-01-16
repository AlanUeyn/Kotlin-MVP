package com.task.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.task.data.DataRepository
import com.task.data.remote.ServiceError
import com.task.data.remote.ServiceResponse
import com.task.data.remote.dto.NewsItem
import com.task.data.remote.dto.NewsModel
import com.task.ui.base.listeners.BaseCallback
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


/**
 * Created by AhmedEltaher on 5/12/2016
 */

class NewsUseCase @Inject
constructor(private val dataRepository: DataRepository, override val coroutineContext: CoroutineContext) : UseCase, CoroutineScope {
    private var newsModle: LiveData<NewsModel>? = null


    fun getNewsAsync(): LiveData<NewsModel>? {
        return runBlocking<LiveData<NewsModel>?> {
            val serviceResponse = async(Dispatchers.IO) { dataRepository.requestNews() }.await()
            if (serviceResponse?.value?.code == ServiceError.SUCCESS_CODE) {
                Transformations.switchMap(serviceResponse) { serviceResponse -> transformServiceResponseToDataObject(serviceResponse) }
            } else {
                null
            }
        }
    }


    override fun getNews(callback: BaseCallback) {
        launch {
            try {
                val serviceResponse = async(Dispatchers.IO) { dataRepository.requestNews() }.await()
                if (serviceResponse?.value?.code == ServiceError.SUCCESS_CODE) {
                    newsModle = Transformations.switchMap(serviceResponse) { serviceResponse -> transformServiceResponseToDataObject(serviceResponse) }
                } else {
                    callback.onFail()
                }
            } catch (e: Exception) {
                callback.onFail()
            }
        }
    }

    override fun searchByTitle(news: List<NewsItem>, keyWord: String): NewsItem? {
        for (newsItem in news) {
            if (!newsItem.title.isNullOrEmpty() && newsItem.title!!.toLowerCase().contains(keyWord.toLowerCase())) {
                return newsItem
            }
        }
        return null
    }

    private fun transformServiceResponseToDataObject(serviceResponse: ServiceResponse): MutableLiveData<NewsModel> {
        var newsModel: NewsModel = serviceResponse.data as NewsModel
        var newsResponse: MutableLiveData<NewsModel> = MutableLiveData()
        newsResponse.value = newsModel
        return newsResponse
    }
}
