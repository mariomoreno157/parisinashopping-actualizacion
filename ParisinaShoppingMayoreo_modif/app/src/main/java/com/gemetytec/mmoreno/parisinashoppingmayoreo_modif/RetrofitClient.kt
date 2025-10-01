package com.gemetytec.mmoreno.parisinashoppingmayoreo_modif

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitClient {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.copomex.com/query/info_cp/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()



    val consumirApi = retrofit.create(ConsumirApi::class.java)
}