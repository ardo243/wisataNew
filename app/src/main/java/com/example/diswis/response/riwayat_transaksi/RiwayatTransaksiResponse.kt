package com.example.diswis.response.riwayat_transaksi

import com.google.gson.annotations.SerializedName

data class RiwayatTransaksiResponse(
    @SerializedName("status")
    val status: Boolean,

    @SerializedName("message")
    val message: String?,

    @SerializedName("data")
    val data: List<Data>?
)
