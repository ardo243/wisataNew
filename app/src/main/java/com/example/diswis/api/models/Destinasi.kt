package com.example.diswis.api.models

import com.google.gson.annotations.SerializedName

data class Destinasi(

    @SerializedName("id")
    val id: String,

    @SerializedName("nama_wisata")
    val namaWisata: String,

    @SerializedName("diskripsi")
    val diskripsi: String,

    @SerializedName("lokasi")
    val lokasi: String,

    @SerializedName("harga_masuk")
    val hargaMasuk: String,

    @SerializedName("jam_operasional")
    val jamOperasional: String,

    @SerializedName("gambar")
    val gambar: String
)
