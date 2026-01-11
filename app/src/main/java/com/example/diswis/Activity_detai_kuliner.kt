package com.example.diswis

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.diswis.api.ApiClient
import com.example.diswis.response.kuliner.Data
import com.example.diswis.response.kuliner.ResponKuliner
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Activity_detai_kuliner : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detai_kuliner)

        // Setup Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
        toolbar.setNavigationOnClickListener { onBackPressed() }

        // Get ID from Intent
        val id = intent.getIntExtra("id_kuliner", -1)

        if (id != -1) {
            fetchDetail(id)
            getDaftarUlasan(id)
            setupSendComment(id)
        } else {
            Toast.makeText(this, "ID Kuliner tidak valid", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun fetchDetail(id: Int) {
        ApiClient.instance.getDetailKuliner(id).enqueue(object : Callback<ResponKuliner> {
            override fun onResponse(call: Call<ResponKuliner>, response: Response<ResponKuliner>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.status == true) {
                        val dataList = responseBody.data
                        if (!dataList.isNullOrEmpty()) {
                            // Assuming getDetailKuliner returns list, take first item
                            bindData(dataList[0])
                        } else {
                            Toast.makeText(this@Activity_detai_kuliner, "Data Kuliner Kosong", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@Activity_detai_kuliner, "Gagal memuat detail", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@Activity_detai_kuliner, "Error API: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponKuliner>, t: Throwable) {
                Toast.makeText(this@Activity_detai_kuliner, "Koneksi Gagal: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun bindData(data: Data) {
        findViewById<TextView>(R.id.tvDetailName).text = data.namaTempat
        findViewById<TextView>(R.id.tvDetailLocation).text = data.lokasi
        findViewById<TextView>(R.id.tvDetailHours).text = data.jamOp
        findViewById<TextView>(R.id.tvDetailDesc).text = data.deskripsi ?: "Tidak ada deskripsi."

        val ivDetailImage = findViewById<ImageView>(R.id.ivDetailImage)
        val imgBaseUrl = "http://10.0.2.2/api_wisata/gambar/"

        Picasso.get()
            .load(imgBaseUrl + data.gambar)
            .placeholder(R.drawable.candi_prambanan)
            .error(android.R.color.darker_gray)
            .into(ivDetailImage)
    }

    private fun getDaftarUlasan(id: Int) {
        val recyclerView = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rv_ulasan)
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)

        // Using "kuliner" type for reviews
        ApiClient.instance.getUlasan(idWisata = id, tipe = "kuliner").enqueue(object : Callback<com.example.diswis.response.ulasan.Ulasanpengguna> {
            override fun onResponse(
                call: Call<com.example.diswis.response.ulasan.Ulasanpengguna>,
                response: Response<com.example.diswis.response.ulasan.Ulasanpengguna>
            ) {
                if (response.isSuccessful && response.body()?.status == true) {
                    val dataUlasan = response.body()?.listUlasan
                    if (dataUlasan != null) {
                        val adapter = UlasanAdapter(dataUlasan)
                        recyclerView.adapter = adapter
                    }
                }
            }

            override fun onFailure(call: Call<com.example.diswis.response.ulasan.Ulasanpengguna>, t: Throwable) {
                Log.e("DetailKuliner", "Gagal ambil ulasan: ${t.message}")
            }
        })
    }

    private fun setupSendComment(idKuliner: Int) {
        val btnKirim = findViewById<ImageView>(R.id.btnsentComment)
        val etKomentar = findViewById<EditText>(R.id.et_komentar)

        btnKirim.setOnClickListener {
            val komentar = etKomentar.text.toString().trim()
            if (komentar.isNotEmpty()) {
                val idUser = 1 // Dummy user ID
                
                ApiClient.instance.kirimUlasan(
                    idUser = idUser,
                    idWisata = idKuliner, // Reusing parameter name idWisata in API for ID
                    komentar = komentar,
                    tipe = "kuliner"
                ).enqueue(object : Callback<Map<String, Any>> {
                    override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@Activity_detai_kuliner, "Ulasan terkirim!", Toast.LENGTH_SHORT).show()
                            etKomentar.setText("")
                            getDaftarUlasan(idKuliner)
                        } else {
                            Toast.makeText(this@Activity_detai_kuliner, "Gagal kirim ulasan", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                        Toast.makeText(this@Activity_detai_kuliner, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this, "Tulis ulasan dulu!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}