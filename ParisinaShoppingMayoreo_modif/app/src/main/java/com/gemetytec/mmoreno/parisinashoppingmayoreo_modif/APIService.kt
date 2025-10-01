package com.gemetytec.mmoreno.parisinashoppingmayoreo_modif

import com.gemetytec.mmoreno.parisinashoppingmayoreo_modif.Models.CodigoPostalItem
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface APIService {
    @GET
  //  suspend fun getCP_datos(@Url url:String):Response<CpResponse>
   // suspend fun getCP_datos(@Url url:String):Response<CpIngresadoItem>
    // fun getCP_datos(@Url url: String): Call<CpIngresadoItem?>?
    fun getCP_datos(@Url url: String): Call<CodigoPostalItem?>?

}