package com.example.diswis

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.diswis.api.ApiClient
import com.example.diswis.response.Detail_Transaksi.DetailTransaksiResponse
import com.example.diswis.response.Transaksi.ResponseTransaksi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.util.Locale

class PembayaranActivity : AppCompatActivity() {

    private var totalAmount: Int = 0
    private var cashAmount: Int = 0
    private var ticketCount: Int = 0
    private var selectedDate: String = ""
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pembayaran)
        
        sessionManager = SessionManager(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.header)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, systemBars.top, 0, v.paddingBottom)
            insets
        }

        // Get Data from Intent
        val totalAmountInt = intent.getIntExtra("EXTRA_TOTAL_AMOUNT", -1)
        ticketCount = intent.getIntExtra("EXTRA_TICKET_COUNT", 1)
        selectedDate = intent.getStringExtra("EXTRA_DATE") ?: ""
        
        if (totalAmountInt != -1) {
            totalAmount = totalAmountInt
        } else {
            val priceString = intent.getStringExtra("EXTRA_TOTAL_PRICE") ?: "Rp 192.500"
            totalAmount = parsePrice(priceString)
        }
        
        // UI Components
        val tvTotalBayar = findViewById<TextView>(R.id.tv_total_bayar)
        val etTunai = findViewById<EditText>(R.id.et_tunai)
        val tvKembalian = findViewById<TextView>(R.id.tv_kembalian)
        val btnSelesai = findViewById<Button>(R.id.btn_selesai)
        
        if (tvTotalBayar != null) {
            tvTotalBayar.text = formatCurrency(totalAmount)
        }

        if (tvKembalian != null) {
             updateChange(etTunai.text.toString(), tvKembalian)
        }

        etTunai.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (tvKembalian != null) {
                    updateChange(s.toString(), tvKembalian)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })


        btnSelesai.setOnClickListener {
            if (cashAmount >= totalAmount) {
                processTransaction()
            } else {
                Toast.makeText(this, "Uang tunai tidak cukup.", Toast.LENGTH_SHORT).show()
            }
        }
        
        setupBottomNav()
    }

    private fun processTransaction() {
        val email = sessionManager.getEmail()
        if (email == null) {
            Toast.makeText(this, "ERROR: Sesi hilang (email null). Silakan login ulang.", Toast.LENGTH_LONG).show()
            return
        }

        // 1. Post Transaction Header (Email + Tanggal)
        ApiClient.instance.pesan(email, selectedDate).enqueue(object : Callback<ResponseTransaksi> {
            override fun onResponse(call: Call<ResponseTransaksi>, response: Response<ResponseTransaksi>) {
                if (response.isSuccessful) {
                    val status = response.body()?.status
                    if (status == true) {
                        val idDetail = response.body()?.data?.id_detail
                        if (idDetail != null && idDetail != 0) {
                             // 2. Post Transaction Details with ID
                            postDetail(idDetail.toString())
                        } else {
                             Toast.makeText(this@PembayaranActivity, "ERROR API 1: id_transaksi kosong. Cek Response PHP.", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(this@PembayaranActivity, "ERROR API 1: Status False. Msg: ${response.body()?.message}", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this@PembayaranActivity, "ERROR API 1 Gagal: Code ${response.code()} - ${response.message()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ResponseTransaksi>, t: Throwable) {
                Toast.makeText(this@PembayaranActivity, "ERROR KONEKSI API 1: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
    
   
    private fun postDetail(idTransaksi: String) {
        ApiClient.instance.postDetailTransaksi(
            idTransaksi = idTransaksi,
            tanggal = selectedDate,
            jumlahTiket = ticketCount.toString(),
            totalHarga = totalAmount.toString()
        ).enqueue(object : Callback<DetailTransaksiResponse> {
            override fun onResponse(
                call: Call<DetailTransaksiResponse>,
                response: Response<DetailTransaksiResponse>
            ) {
                 if (response.isSuccessful) {
                     val body = response.body()
                     if (body?.status == true) {
                        Toast.makeText(this@PembayaranActivity, "Pembayaran Berhasil Disimpan!", Toast.LENGTH_LONG).show()
                        navigateToHome()
                     } else {
                         Toast.makeText(this@PembayaranActivity, "ERROR API 2: Status False. Msg: ${body?.message}", Toast.LENGTH_LONG).show()
                     }
                } else {
                     Toast.makeText(this@PembayaranActivity, "ERROR API 2 Gagal: Code ${response.code()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<DetailTransaksiResponse>, t: Throwable) {
                 Toast.makeText(this@PembayaranActivity, "ERROR KONEKSI API 2: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun navigateToHome() {
        val intent = Intent(this, ActivityHome::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }
    
    private fun updateChange(cashString: String, tvKembalian: TextView) {
        try {
            cashAmount = if (cashString.isNotEmpty()) cashString.toInt() else 0
            val change = cashAmount - totalAmount
            tvKembalian.text = formatCurrency(change)
             if (change < 0) {
                 tvKembalian.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark))
             } else {
                 tvKembalian.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark))
             }
        } catch (e: NumberFormatException) {
            cashAmount = 0
            tvKembalian.text = formatCurrency(0 - totalAmount)
            tvKembalian.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark))
        }
    }

    private fun parsePrice(priceDiff: String): Int {
        return try {
            priceDiff.replace("Run", "", ignoreCase = true)
                .replace("Rp", "", ignoreCase = true)
                .replace(".", "")
                .replace(" ", "")
                .trim()
                .toInt()
        } catch (e: Exception) {
            192500 // Fallback
        }
    }

    private fun formatCurrency(amount: Int): String {
        val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        return format.format(amount).replace("Rp", "Rp ")
    }
    
    private fun setupBottomNav() {
         val navHome = findViewById<ImageView>(R.id.nav_home)
         navHome?.setOnClickListener {
            navigateToHome()
        }
    }
}
