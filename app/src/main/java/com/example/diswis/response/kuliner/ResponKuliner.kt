package com.example.diswis.response.kuliner

import com.google.gson.annotations.SerializedName

data class ResponKuliner(
    @SerializedName("data")
    val data: List<Data>?, // Gunakan List<Data>? jika ini adalah daftar kuliner

    @SerializedName("message")
    val message: String?,

    @SerializedName("status")
    val status: Boolean?
)
