package com.example.diswis.response.paket

import com.google.gson.annotations.SerializedName

data class PaketRespon(
    @SerializedName("data")
    val data: List<Data>?, // Gunakan List jika datanya banyak, gunakan ? agar aman dari null

    @SerializedName("message")
    val message: String?,

    @SerializedName("status")
    val status: Boolean?
)
