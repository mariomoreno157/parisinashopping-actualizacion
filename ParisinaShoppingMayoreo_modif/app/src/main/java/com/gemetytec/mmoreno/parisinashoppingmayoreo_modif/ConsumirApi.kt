package com.gemetytec.mmoreno.parisinashoppingmayoreo_modif

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface ConsumirApi {
   // @GET("28983")

  //  fun getTraer() : Call<CpIngresadoItem>

    @GET
    suspend  fun getTraer(@Url url:String):Response<CpIngresadoItem>
}