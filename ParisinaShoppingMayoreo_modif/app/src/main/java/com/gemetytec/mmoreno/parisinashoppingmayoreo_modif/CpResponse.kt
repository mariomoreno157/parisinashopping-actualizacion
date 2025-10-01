package com.gemetytec.mmoreno.parisinashoppingmayoreo_modif

import com.google.gson.annotations.SerializedName

data class CpResponse(
    @SerializedName("error") var error_ : String,
    @SerializedName("code_error") var codigo_error :String,
    @SerializedName("error_message") var mensaje_error : String,
    @SerializedName("response") var datos_cp : List<String>
    )