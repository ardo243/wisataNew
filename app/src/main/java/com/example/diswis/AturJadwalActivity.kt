package com.example.diswis

import android.os.Bundle
import android.widget.CalendarView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Button
import android.widget.Toast
import android.content.Intent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.NumberFormat
import java.util.Locale

class AturJadwalActivity : AppCompatActivity() {

    private var basePrice: Int = 0
    private var ticketCount: Int = 1
    // Removed separate child count as per new UI design which shows a single "Total Tiket"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_atur_jadwal)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.header)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, systemBars.top, 0, v.paddingBottom)
            insets
        }

        // Get data from intent
        val title = intent.getStringExtra("EXTRA_TITLE") ?: "Paket Wisata"
        val priceString = intent.getStringExtra("EXTRA_PRICE") ?: "0k"
        val imageResId = intent.getIntExtra("EXTRA_IMAGE", R.drawable.candi_prambanan)

        // Parse price (assuming "80k" format -> 80000)
        basePrice = parsePrice(priceString)

        // Setup UI
        findViewById<ImageView>(R.id.btn_back).setOnClickListener { finish() }
        findViewById<TextView>(R.id.tv_package_name_detail).text = title
        // Price detail removed from card in new design or can be added back if needed, currently not in mapped IDs of new XML for card detail
        // But we have total price at bottom
        
        findViewById<ImageView>(R.id.img_selected_package).setImageResource(imageResId)

        setupCounters()
        updateTotalPrice()
        
        findViewById<Button>(R.id.btn_pesan_sekarang).setOnClickListener {
            // Toast.makeText(this, "Pesanan berhasil dibuat!", Toast.LENGTH_SHORT).show()
            val totalPrice = findViewById<TextView>(R.id.tv_total_price).text.toString()
            
            val intent = Intent(this, PembayaranActivity::class.java)
            intent.putExtra("EXTRA_TITLE", title)
            intent.putExtra("EXTRA_TOTAL_PRICE", totalPrice)
            intent.putExtra("EXTRA_IMAGE", imageResId)
            startActivity(intent)
        }
    }

    private fun parsePrice(priceDiff: String): Int {
        return try {
            priceDiff.replace("k", "000").replace("K", "000").replace("Rp", "").replace(".", "").replace(" ", "").toInt()
        } catch (e: Exception) {
            0
        }
    }

    private fun setupCounters() {
        val tvCount = findViewById<TextView>(R.id.tv_count_adult) // Reused ID for general ticket count
        tvCount.text = ticketCount.toString()

        findViewById<ImageView>(R.id.btn_minus_adult).setOnClickListener {
            if (ticketCount > 1) {
                ticketCount--
                tvCount.text = ticketCount.toString()
                updateTotalPrice()
            }
        }

        findViewById<ImageView>(R.id.btn_plus_adult).setOnClickListener {
            ticketCount++
            tvCount.text = ticketCount.toString()
            updateTotalPrice()
        }
    }

    private fun updateTotalPrice() {
        val total = ticketCount * basePrice
        
        val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        findViewById<TextView>(R.id.tv_total_price).text = format.format(total).replace("Rp", "Rp ")
        findViewById<TextView>(R.id.tv_total_tickets_label).text = "$ticketCount Tiket"
    }
}
