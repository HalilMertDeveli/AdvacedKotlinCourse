package com.atilsamancioglu.besinlerkitabigradlework.service

import com.atilsamancioglu.besinlerkitabigradlework.model.Food
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class FoodApiService {
    //you need to get http after raw
    //https://raw.githubusercontent.com/atilsamancioglu/btk20-jsonveriseti/master/besinler.json

    //Base URL = https://raw.githubusercontent.com/
    //Data URL = atilsamancioglu/btk20-jsonveriseti/master/besinler.json
    private val BASE_URL = "https://raw.githubusercontent.com/"
    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)//we gave the url
        .addConverterFactory(GsonConverterFactory.create())//for json to string
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//for using Rx java
        .build()//for building api
        .create(FoodApi::class.java)//for creating api

    fun getData():Single<List<Food>>{
        return api.getFoods()//we accessed to interface with above api
    }
}