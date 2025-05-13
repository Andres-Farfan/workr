package com.example.workr

data class BusinessRegisterRequest(
    val nombre: String,
    val sector: String,
    val numeroEmpleados: Int,
    val tipo: String
)
