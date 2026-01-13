package com.example.diswis

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.diswis.api.ApiClient
import com.example.diswis.response.riwayat_transaksi.RiwayatTransaksiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentPesanPaket : Fragment() {

    private lateinit var rvTiket: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvEmpty: TextView
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pesan_paket, container, false)
        
        rvTiket = view.findViewById(R.id.rv_tiket)
        progressBar = view.findViewById(R.id.progress_bar)
        tvEmpty = view.findViewById(R.id.tv_empty)
        sessionManager = SessionManager(requireContext())

        rvTiket.layoutManager = LinearLayoutManager(context)

        loadTiket()

        // BOTTOM NAVIGATION LOGIC
        val navHome = view.findViewById<android.widget.ImageView>(R.id.nav_home)
        navHome.setOnClickListener {
            startActivity(android.content.Intent(context, ActivityHome::class.java))
        }

        val navTicket = view.findViewById<android.widget.ImageView>(R.id.nav_paket)
        navTicket.setOnClickListener {
             // Already here
        }

        val navFav = view.findViewById<android.widget.ImageView>(R.id.nav_fav)
        navFav.setOnClickListener {
            startActivity(android.content.Intent(context, WishlistActivity::class.java))
        }

        val navProfile = view.findViewById<android.widget.ImageView>(R.id.nav_profile)
        navProfile.setOnClickListener {
            startActivity(android.content.Intent(context, ProfileActivity::class.java))
        }

        return view
    }

    private fun loadTiket() {
        val email = sessionManager.getEmail()
        if (email.isNullOrEmpty()) {
            Toast.makeText(context, "Silakan login terlebih dahulu", Toast.LENGTH_SHORT).show()
            return
        }

        // DEBUG: Cek Email yang dikirim
        Toast.makeText(context, "Cek Riwayat: Email $email", Toast.LENGTH_LONG).show()

        progressBar.visibility = View.VISIBLE
        ApiClient.instance.getRiwayat(email).enqueue(object : Callback<RiwayatTransaksiResponse> {
            override fun onResponse(
                call: Call<RiwayatTransaksiResponse>,
                response: Response<RiwayatTransaksiResponse>
            ) {
                try {
                    progressBar.visibility = View.GONE
                    if (response.isSuccessful) {
                        val body = response.body()
                        val listData = body?.data
                        
                        // DEBUG: Cek Berapa Data yang diterima
                        val jumlah = listData?.size ?: 0
                        Toast.makeText(context, "Dapat Data: $jumlah tiket", Toast.LENGTH_LONG).show()

                        if (!listData.isNullOrEmpty()) {
                            rvTiket.adapter = AdapterTiket(listData)
                            tvEmpty.visibility = View.GONE
                        } else {
                            tvEmpty.visibility = View.VISIBLE
                            tvEmpty.text = "Data kosong.\nStatus: ${body?.status}\nMsg: ${body?.message}"
                        }
                    } else {
                        Toast.makeText(context, "Gagal memuat: ${response.code()} ${response.message()}", Toast.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, "Error UI: ${e.message}", Toast.LENGTH_LONG).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<RiwayatTransaksiResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(context, "Error koneksi: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}