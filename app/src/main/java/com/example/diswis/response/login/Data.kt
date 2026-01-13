package com.example.diswis.response.login

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("email")
    val email: String,

    @SerializedName("id_user")
    val idUser: String, // Menggunakan camelCase agar sesuai standar Kotlin

    @SerializedName("logged_in")
    val loggedIn: Boolean,

    @SerializedName("no_telpon")
    val noTelpon: String?,

    @SerializedName("username")
    val username: String
)