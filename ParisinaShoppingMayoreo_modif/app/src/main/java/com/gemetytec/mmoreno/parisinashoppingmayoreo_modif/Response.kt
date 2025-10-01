package com.gemetytec.mmoreno.parisinashoppingmayoreo_modif

import com.google.gson.annotations.SerializedName

data class Response(
    val cp: String,
    val asentamiento: String,
    val tipo_asentamiento: String,
    val municipio: String,
    val estado: String,
    val ciudad: String,
    val pais: String
)

//response":{"cp":"28983","asentamiento":"ValledelSol","tipo_asentamiento":"Fraccionamiento","municipio":"VilladeÁlvarez","estado":"Colima","ciudad":"CiudaddeVilladeÁlvarez","pais":"México"}

