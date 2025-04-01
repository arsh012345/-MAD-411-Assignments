package com.example.assignment_6.network

import retrofit2.http.GET

interface ApiService{
    @GET("latest/USD")              //using get to put converstion rates
    suspend fun getRates(): ResposeForConverstion     //function getRates for converstion
}
