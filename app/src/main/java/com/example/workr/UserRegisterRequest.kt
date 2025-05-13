package com.example.workr

data class UserRegisterRequest(
    val firstName: String,
    val lastName: String,
    val contact: String,
    val password: String,
    val country: String,
    val position: String
)