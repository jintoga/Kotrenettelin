package com.dat.kotrenettelin.api

import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Dat on 8/11/2017.
 */
class ImageBundleService {
    private val ENDPOINT = "https://api.myjson.com"
    private var imageBundleApi: ImageBundleApi

    init {
        val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ENDPOINT)
                .build()
        imageBundleApi = retrofit.create(ImageBundleApi::class.java)
    }

    fun getApi(): ImageBundleApi {
        return imageBundleApi
    }

}