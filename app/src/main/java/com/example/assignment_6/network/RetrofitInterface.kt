package com.example.assignment_6.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInterface{
    private const val BASE_URL = "https://v6.exchangerate-api.com/v6/e588eceef6462f4d62c57c79/"
    //using my own created free api from exchangerate-api.com

    val api: ApiService by lazy {                   //api created here
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}