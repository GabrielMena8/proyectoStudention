package com.example.studention



//Respuesta de Base

data class BaseResponse(
    val Welcome: String
)

data class VoteResponse(
    val id: Int,
    val code: Int,
    val boton1: Boolean,
    val boton2: Boolean
)