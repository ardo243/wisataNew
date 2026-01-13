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
        val idWisata = intent.getStringExtra("EXTRA_ID_WISATA") ?: ""

        // Parse price (assuming "80k" format -> 80000)
        basePrice = parsePrice(priceString)

        // Setup UI
        findViewById<ImageView>(R.id.btn_back).setOnClickListener { finish() }
        findViewById<TextView>(R.id.tv_package_name_detail).text = title
        // Price detail removed from card in new design or can be added back if needed, currently not in mapped IDs of new XML for card detail
        // But we have total price at bottom
        
        findViewById<ImageView>(R.id.img_selected_package).setImageResource(imageResId)

        // Setup Counters
        setupCounters()
        updateTotalPrice()

        var selectedDate = ""
        val calendarView = findViewById<CalendarView>(R.id.calendarView)
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            // Format: YYYY-MM-DD
            selectedDate = "$year-${month + 1}-$dayOfMonth"
        }

        // Set default date to today if not selected
        if (selectedDate.isEmpty()) {
            val calendar = java.util.Calendar.getInstance()
            selectedDate = "${calendar.get(java.util.Calendar.YEAR)}-${calendar.get(java.util.Calendar.MONTH) + 1}-${calendar.get(java.util.Calendar.DAY_OF_MONTH)}"
        }
        
        findViewById<Button>(R.id.btn_pesan_sekarang).setOnClickListener {
            // Recalculate total to be sure
            val total = ticketCount * basePrice
            val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
            val totalPriceString = format.format(total).replace("Rp", "Rp ")

            val intent = Intent(this, PembayaranActivity::class.java)
            intent.putExtra("EXTRA_TITLE", title)
            intent.putExtra("EXTRA_TOTAL_PRICE", totalPriceString) // Keep for compatibility if needed, but we will use INT preferably
            intent.putExtra("EXTRA_TOTAL_AMOUNT", total) // Pass raw int
            intent.putExtra("EXTRA_IMAGE", imageResId)
            intent.putExtra("EXTRA_DATE", selectedDate)
            intent.putExtra("EXTRA_TICKET_COUNT", ticketCount)
            intent.putExtra("EXTRA_ID_WISATA", idWisata)
            startActivity(intent)
        }
    }

    private fun parsePrice(priceDiff: String): Int {
        return try {
            var cleanPrice = priceDiff.replace("Rp", "").replace(" ", "").lowercase()
            
            if (cleanPrice.contains("jt")) {
                cleanPrice = cleanPrice.replace("jt", "")
                val value = cleanPrice.toDoubleOrNull() ?: 0.0
                (value * 1_000_000).toInt()
            } else if (cleanPrice.contains("k")) {
                 cleanPrice = cleanPrice.replace("k", "")
                 // If there's a dot or comma in "80.5k" or similar? Assuming simple cases for now or standard format
                 cleanPrice = cleanPrice.replace(".", "") // remove thousands separator points if any, BUT care if it is decimal
                 // Actually standard "80k" is 80000. "1.5jt" is 1500000.
                 val value = cleanPrice.toIntOrNull() ?: 0
                 value * 1000
            } else {
                 // Standard number parsing
                 cleanPrice = cleanPrice.replace(".", "") // Remove points used as thousands separators in ID format
                 cleanPrice.toInt()
            }
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
