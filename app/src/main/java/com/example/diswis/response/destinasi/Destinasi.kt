package com.example.diswis.response.destinasi

import com.google.gson.annotations.SerializedName

data class Destinasi(
    @SerializedName("status")
    val status: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val dataList: List<Data>
)
