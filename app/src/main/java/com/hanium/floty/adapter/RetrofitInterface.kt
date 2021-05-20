package com.hanium.floty.adapter

import com.hanium.floty.model.Plant
import retrofit2.Call
import retrofit2.http.GET

var apiKey: String = "" // 인증키
var serviceName: String = "garden"
var operationName = "gardenDtl"
var parameter: String = "/" + serviceName + "/" + operationName + "?apiKey=" + apiKey

interface RetrofitInterface {
    @GET()
    fun requestAllData() : Call<Plant>
}