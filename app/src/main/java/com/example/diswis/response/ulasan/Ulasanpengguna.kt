package com.example.diswis.response.ulasan

import com.google.gson.annotations.SerializedName

data class Ulasanpengguna(
    // Mengarahkan field "data" dari JSON ke variabel listUlasan
    @SerializedName("data")
    val listUlasan: List<Data>,

    @SerializedName("message")
    val message: String,

    @SerializedName("status")
    val status: Boolean
)
