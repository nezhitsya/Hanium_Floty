package com.hanium.floty.adapter

import com.hanium.floty.model.Item
import com.hanium.floty.model.Plant
import retrofit2.Call
import retrofit2.http.GET

var apiKey: String = "20210520NAAIM2CJQYXUPPPTCCBQ" // 인증키
var serviceName: String = "garden"
var operationName = "gardenList"
var parameter: String = serviceName + "/" + operationName + "?apiKey=" + apiKey

interface RetrofitInterface {
    @GET("garden/gardenList?apiKey=20210520NAAIM2CJQYXUPPPTCCBQ")
    fun requestAllData() : Call<Plant>
}