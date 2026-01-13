package com.example.diswis.response.destinasi

import com.google.gson.annotations.SerializedName

data class DetailDestinasiResponse(
    @SerializedName("status")
    val status: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: Data
)
