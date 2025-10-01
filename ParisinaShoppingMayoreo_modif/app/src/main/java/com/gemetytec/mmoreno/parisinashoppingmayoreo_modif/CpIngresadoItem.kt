package com.gemetytec.mmoreno.parisinashoppingmayoreo_modif

data class CpIngresadoItem(
    val code_error: Int,
    val error: Boolean,
    val error_message: Any,
   val response: List<Response>
)