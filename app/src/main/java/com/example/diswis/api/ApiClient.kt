package com.example.diswis.api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val BASE_URL =
        "http://10.0.2.2/api_wisata/index.php/"

    val instance: ApiService by lazy {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        val logging = okhttp3.logging.HttpLoggingInterceptor()
        logging.setLevel(okhttp3.logging.HttpLoggingInterceptor.Level.BODY)

        val client = okhttp3.OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()

        retrofit.create(ApiService::class.java)


    }

}