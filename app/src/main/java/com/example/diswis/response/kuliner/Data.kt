package com.example.diswis.response.kuliner

import com.google.gson.annotations.SerializedName

data class Data(    @SerializedName("id_kuliner")
                    val idKuliner: Int?,

                    @SerializedName("nama_tempat")
                    val namaTempat: String?,

                    @SerializedName("deskripsi")
                    val deskripsi: String?,

                    @SerializedName("gambar")
                    val gambar: String?,

                    @SerializedName("jam_op")
                    val jamOp: String?,

                    @SerializedName("lokasi")
                    val lokasi: String?
)
