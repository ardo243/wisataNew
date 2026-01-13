package com.example.diswis.response.ulasan

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id_ulasan")
    val idUlasan: Int,
    @SerializedName("id_user")
    val idUser: String,
    @SerializedName("id_wisata")
    val idWisata: String,
    @SerializedName("komentar")
    val komentar: String,
    @SerializedName("tipe")
    val tipe: String,
    @SerializedName("username")
    val username: String? // Nullable in case backend doesn't send it yet
)
