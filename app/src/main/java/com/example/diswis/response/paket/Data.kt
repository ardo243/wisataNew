package com.example.diswis.response.paket

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("deskripsi")
    val deskripsi: String?,

    @SerializedName("durasi")
    val durasi: String?,

    @SerializedName("fasilitas")
    val fasilitas: String?,

    @SerializedName("gambar")
    val gambar: String?,

    @SerializedName("harga")
    val harga: String?,

    @SerializedName("id_wisata")
    val idWisata: String?, // Menggunakan camelCase agar standar Kotlin

    @SerializedName("id_paket")
    val idPaket: String?,

    @SerializedName("nama_paket")
    val namaPaket: String? // Menggunakan camelCase
)
