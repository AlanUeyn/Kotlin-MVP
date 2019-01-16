package com.task.ui.component.details

import androidx.lifecycle.MutableLiveData
import com.task.data.remote.dto.NewsItem
import com.task.ui.base.BaseViewModel
import javax.inject.Inject

/**
 * Created by AhmedEltaher on 11/12/16.
 */

class DetailsViewModel @Inject
constructor() : BaseViewModel(), DetailsContract.IViewModel {

     var newsItem: MutableLiveData<NewsItem>? = null

//    override fun initialize(extras: Bundle?) {
//        super.initialize(extras)
//        newsItem = extras?.getParcelable(Constants.NEWS_ITEM_KEY)
//        getView()?.initializeView(newsItem!!)
//    }
}
