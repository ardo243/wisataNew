package com.example.diswis.api

import com.example.diswis.api.models.DestinasiResponse
import com.example.diswis.api.models.LoginResponse
import com.example.diswis.api.models.RegisterResponse
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
        @Field("no_telpon") noTelpon: String,
        @Field("password") password: String
    ): Call<RegisterResponse>


    // GET ALL
    @GET("destinasi")
    fun getDestinasi(): Call<DestinasiResponse>

    // GET DETAIL
    @GET("destinasi/detail/{id}")
    fun getDetailDestinasi(
        @Path("id") id: String
    ): Call<DestinasiResponse>

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
}
