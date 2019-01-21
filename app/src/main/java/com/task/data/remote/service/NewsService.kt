package com.task.data.remote.service

import com.task.data.remote.dto.NewsModel
import retrofit2.Call
import retrofit2.http.GET

/**
 * Created by AhmedEltaher on 5/12/2016
 */

interface NewsService {
    @GET("topstories/v2/home.json?api-key=k5ptm99cGH9NgdIzGIyGhmslFv2cX3Jt")
    fun  fetchNews(): Call<NewsModel>
}
