package com.example.diswis.api

import com.example.diswis.response.destinasi.DetailDestinasiResponse
import com.example.diswis.DetailDestinasiActivity
import com.example.diswis.response.Detail_Transaksi.DetailTransaksiResponse
import com.example.diswis.response.Transaksi.ResponseTransaksi
import com.example.diswis.response.destinasi.Destinasi
import com.example.diswis.response.kuliner.ResponKuliner
import com.example.diswis.response.login.LoginResponse
import com.example.diswis.response.paket.PaketRespon
import com.example.diswis.response.register.RegisterResponse
import com.example.diswis.response.ulasan.Ulasanpengguna
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("auth/login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>
    @FormUrlEncoded
    @POST("auth/register")
    fun register(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("no_telpon") notelpon: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @GET("destinasi")
    fun getDestinasi(): Call<Destinasi>

    @GET("destinasi/detail/{id}")
    fun getDetailDestinasi(
        @Path("id") id: String
    ): Call<DetailDestinasiResponse>

    // CREATE
    @FormUrlEncoded
    @POST("destinasi")
    fun addDestinasi(
        @Field("nama_wisata") namaWisata: String,
        @Field("diskripsi") diskripsi: String,
        @Field("lokasi") lokasi: String,
        @Field("harga_masuk") hargaMasuk: String,
        @Field("jam_op") jamOperasional: String,
        @Field("gambar") gambar: String
    ): Call<Map<String, Any>>

    // UPDATE
    @FormUrlEncoded
    @PUT("destinasi/{id}")
    fun updateDestinasi(
        @Path("id") id: String,
        @Field("nama_wisata") namaWisata: String,
        @Field("diskripsi") diskripsi: String,
        @Field("lokasi") lokasi: String,
        @Field("harga_masuk") hargaMasuk: String,
        @Field("jam_op") jamOperasional: String,
        @Field("gambar") gambar: String
    ): Call<Map<String, Any>>

    // DELETE
    @DELETE("destinasi/{id}")
    fun deleteDestinasi(
        @Path("id") id: String
    ): Call<Map<String, Any>>

    @GET("Ulasan")
    fun getUlasan(
        @Query("id_wisata") idWisata: Int,
        @Query("tipe") tipe: String
    ): Call<Ulasanpengguna>
    @FormUrlEncoded
    @POST("ulasan")
    fun kirimUlasan(
        @Field("id_user") idUser: Int,
        @Field("id_wisata") idWisata: Int,
        @Field("komentar") komentar: String,
        @Field("tipe") tipe: String
    ): Call<Map<String, Any>>

    @GET("kuliner")
    fun getKuliner(): Call<ResponKuliner>
    @GET("kuliner")
    fun getDetailKuliner(
        @Query("id_kuliner") idKuliner: Int
    ): Call<ResponKuliner>

    @FormUrlEncoded
    @POST("kuliner")
    fun kirimKuliner(
        @Field("id_user") idUser: Int,
        @Field("nama_tempat") namaTempat: String,
        @Field("deskripsi") deskripsi: String,
        @Field("lokasi") lokasi: String,
        @Field("jam_op") jamOp: String,
        @Field("gambar") gambar: String // Biasanya berupa String URL atau Base64
    ): Call<Map<String, Any>>
//paket wisata

    // 1. Mengambil semua daftar paket
    @GET("paket")
    fun getPaket(): Call<PaketRespon>


    // 3. Menambah/Kirim data paket baru (Contoh POST)
    @FormUrlEncoded
    @POST("paket")
    fun kirimPaket(
        @Field("nama_paket") namaPaket: String,
        @Field("durasi") durasi: String,
        @Field("harga") harga: String,
        @Field("fasilitas") fasilitas: String,
        @Field("deskripsi") deskripsi: String,
        @Field("gambar") gambar: String,
        @Field("id_wisata") idWisata: String

    ): Call<PaketRespon>
    // 4. Transaksi Header (Step 1)
    @FormUrlEncoded
    @POST("transaksi") 
    fun pesan(
        @Field("email") email: String,
        @Field("tanggal_pesan") tanggal: String
    ): Call<ResponseTransaksi>

    // 5. Transaksi Detail (Step 2)
    @FormUrlEncoded
    @POST("detail_transaksi")
    fun postDetailTransaksi(
        @Field("id_transaksi") idTransaksi: String,
        @Field("id_wisata") idWisata: String,
        @Field("tanggal") tanggal: String,
        @Field("jumlah_tiket") jumlahTiket: String,
        @Field("total_harga") totalHarga: String
    ): Call<DetailTransaksiResponse>


    // 6. Get Riwayat Transaksi
    @GET("riwayat")
    fun getRiwayat(
        @Query("email") email: String
    ): Call<com.example.diswis.response.riwayat_transaksi.RiwayatTransaksiResponse>
}
