package com.example.diswis.response.riwayat_transaksi

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("id_transaksi")
    val idTransaksi: String,

    @SerializedName("tanggal_pesan")
    val tanggalPesan: String,

    @SerializedName("tanggal_wisata")
    val tanggalWisata: String,

    @SerializedName("jumlah_tiket")
    val jumlahTiket: String,

    @SerializedName("total_harga")
    val totalHarga: String,

    @SerializedName("nama_wisata")
    val namaWisata: String?, // Nullable if missing in DB
    
    @SerializedName("nama_paket")
    val namaPaket: String?,

    @SerializedName("gambar")
    val gambar: String?
)
