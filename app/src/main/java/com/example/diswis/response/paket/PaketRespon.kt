package com.example.diswis.response.paket

import com.google.gson.annotations.SerializedName

data class PaketRespon(
    @SerializedName("data")
    val data: List<Data>?,

    @SerializedName("message")
    val message: String?,

    @SerializedName("status")
    val status: Boolean?
)
