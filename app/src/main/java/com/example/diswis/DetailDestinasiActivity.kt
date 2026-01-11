package com.example.diswis

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.diswis.api.ApiClient
import com.example.diswis.response.destinasi.Data
import com.example.diswis.response.destinasi.Destinasi
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log
import com.example.diswis.response.destinasi.DetailDestinasiResponse

class DetailDestinasiActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_destinasi)

        // Setup Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "" // Judul kosong agar tidak menumpuk dengan CollapsingToolbar
        toolbar.setNavigationOnClickListener { onBackPressed() }

        // Ambil ID dari Intent (Pastikan di Adapter dikirim dengan key "id_wisata")
        val id = intent.getStringExtra("id_wisata")

        if (id != null) {
            fetchDetail(id)
            getDaftarUlasan(id)
            setupSendComment(id)
        } else {
            Toast.makeText(this, "ID Wisata tidak valid", Toast.LENGTH_SHORT).show()
            finish()
        }
    }



    private fun fetchDetail(id: String) {
        ApiClient.instance.getDetailDestinasi(id).enqueue(object : Callback<DetailDestinasiResponse> {
            override fun onResponse(call: Call<DetailDestinasiResponse>, response: Response<DetailDestinasiResponse>) {
                Log.d("DetailActivity", "Response Code: ${response.code()}")
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.d("DetailActivity", "Body: $responseBody")
                    
                    if (responseBody != null && responseBody.status) {
                        val data = responseBody.data
                        if (data != null) {
                            bindData(data)
                        } else {
                            Log.e("DetailActivity", "Data Object is Null")
                            Toast.makeText(this@DetailDestinasiActivity, "Data Kosong di Server", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Log.e("DetailActivity", "Response Status False or Body Null")
                        Toast.makeText(this@DetailDestinasiActivity, "Status Response False", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("DetailActivity", "Response Not Successful: ${response.message()}")
                    Toast.makeText(this@DetailDestinasiActivity, "Error API: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DetailDestinasiResponse>, t: Throwable) {
                Log.e("DetailActivity", "OnFailure: ${t.message}")
                Toast.makeText(this@DetailDestinasiActivity, "Koneksi Gagal: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun bindData(data: Data) {
        // Hubungkan dengan ID XML
        findViewById<TextView>(R.id.tvDetailName).text = data.nama_wisata
        findViewById<TextView>(R.id.tvDetailLocation).text = data.lokasi
        findViewById<TextView>(R.id.tvDetailPrice).text = "Rp ${data.harga_masuk}"
        findViewById<TextView>(R.id.tvDetailHours).text = data.jam_op

        // Deskripsi (Gunakan toString() atau handle jika null)
        findViewById<TextView>(R.id.tvDetailDesc).text = data.deskripsi?.toString() ?: "Tidak ada deskripsi."

        val ivDetailImage = findViewById<ImageView>(R.id.ivDetailImage)
        val imgBaseUrl = "http://10.0.2.2/api_wisata/gambar/"

        Picasso.get()
            .load(imgBaseUrl + data.gambar)
            .placeholder(R.drawable.candi_prambanan)
            .error(android.R.color.darker_gray) // Gunakan warna jika gambar tidak ada di server
            .into(ivDetailImage)
    }

    private fun getDaftarUlasan(id: String) {
        val recyclerView = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rv_ulasan)
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)

        val idInt = id.toIntOrNull() ?: return

        // Menggunakan ApiClient.instance (bukan ApiConfig) dan parameter id yang dinamis
        ApiClient.instance.getUlasan(idWisata = idInt, tipe = "wisata").enqueue(object : Callback<com.example.diswis.response.ulasan.Ulasanpengguna> {
            override fun onResponse(
                call: Call<com.example.diswis.response.ulasan.Ulasanpengguna>,
                response: Response<com.example.diswis.response.ulasan.Ulasanpengguna>
            ) {
                if (response.isSuccessful && response.body()?.status == true) {
                    // Perbaikan: Gunakan .listUlasan (sesuai model), bukan .data
                    val dataUlasan = response.body()?.listUlasan
                    if (dataUlasan != null) {
                        val adapter = UlasanAdapter(dataUlasan)
                        recyclerView.adapter = adapter
                    }
                }
            }

            override fun onFailure(call: Call<com.example.diswis.response.ulasan.Ulasanpengguna>, t: Throwable) {
                Log.e("DetailActivity", "Gagal ambil ulasan: ${t.message}")
            }
        })
    }

    private fun setupSendComment(idWisata: String) {
        val btnKirim = findViewById<ImageView>(R.id.btnsentComment)
        val etKomentar = findViewById<android.widget.EditText>(R.id.et_komentar)

        btnKirim.setOnClickListener {
            val komentar = etKomentar.text.toString().trim()
            if (komentar.isNotEmpty()) {
                // Gunakan ID Dummy 12 (Int) karena SessionManager belum simpan ID
                val idUser = 1
                val idWisataInt = idWisata.toIntOrNull() ?: 0
                
                ApiClient.instance.kirimUlasan(
                    idUser = idUser,
                    idWisata = idWisataInt,
                    komentar = komentar,
                    tipe = "wisata"
                ).enqueue(object : Callback<Map<String, Any>> {
                    override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@DetailDestinasiActivity, "Ulasan terkirim!", Toast.LENGTH_SHORT).show()
                            etKomentar.setText("")
                            getDaftarUlasan(idWisata) // Refresh list ulasan
                        } else {
                            Toast.makeText(this@DetailDestinasiActivity, "Gagal kirim: ${response.message()}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                        Toast.makeText(this@DetailDestinasiActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this, "Tulis ulasan dulu!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
