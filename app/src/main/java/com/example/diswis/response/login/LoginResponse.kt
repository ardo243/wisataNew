package com.example.diswis.response.login

data class LoginResponse(
    val status: Boolean,
    val message: String,
    val data: UserData
)

data class UserData(
    val id_user: String,
    val username: String,
    val email: String,
    val level: String,
    val logged_in: Boolean
)