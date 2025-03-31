package com.example.assignment_6.network

import retrofit2.http.GET

interface ApiService{
    @GET("cad.json")                           //using get to put converstions in file
    suspend fun getRates(): ResposeForConverstion      //function getRates for converstion
}
