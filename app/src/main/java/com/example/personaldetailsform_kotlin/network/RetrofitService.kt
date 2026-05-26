package com.example.personaldetailsform_kotlin.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    val api: ApiService = Retrofit
        // starts to build Retrofit configs
            .Builder()
        // all APIs must start with this base URL
            .baseUrl("https://picsum.photos/")
         // convert the received response into Kotlin objs using GSON
            .addConverterFactory(GsonConverterFactory.create())
         // build the retrofit object
            .build()
         // for all interfaces in ApiService create Object
            .create(ApiService::class.java)
}