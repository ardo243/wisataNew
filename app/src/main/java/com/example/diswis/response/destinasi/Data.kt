package com.example.diswis.response.destinasi

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Data(
    @SerializedName("id_wisata")
    val id_wisata: String?,

    @SerializedName("nama_wisata")
    val nama_wisata: String?,

    @SerializedName("lokasi")
    val lokasi: String?,

    @SerializedName("deskripsi")
    val deskripsi: String?,

    @SerializedName("harga_masuk")
    val harga_masuk: String?,

    @SerializedName("jam_op")
    val jam_op: String?,

    @SerializedName("gambar")
    val gambar: String?
): Parcelable
