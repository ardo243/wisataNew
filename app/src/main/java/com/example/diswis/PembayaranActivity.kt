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
import java.text.NumberFormat
import java.util.Locale

class PembayaranActivity : AppCompatActivity() {

    private var totalAmount: Int = 0
    private var cashAmount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pembayaran)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.header)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, systemBars.top, 0, v.paddingBottom)
            insets
        }

        // Get Data from Intent
        // Default to a value if not provided for visual consistency with screenshot
        val priceString = intent.getStringExtra("EXTRA_TOTAL_PRICE") ?: "Rp 192.500"
        
        // Parse Total Amount
        totalAmount = parsePrice(priceString)
        
        // UI Components
        val tvTotalBayar = findViewById<TextView>(R.id.tv_total_bayar)
        val etTunai = findViewById<EditText>(R.id.et_tunai)
        val tvKembalian = findViewById<TextView>(R.id.tv_kembalian)
        val btnSelesai = findViewById<Button>(R.id.btn_selesai)
        
        if (tvTotalBayar != null) {
            tvTotalBayar.text = formatCurrency(totalAmount)
        }

        // Set initial cash value from EditText (default 200000)
        if (tvKembalian != null) {
             updateChange(etTunai.text.toString(), tvKembalian)
        }

        // TextWatcher to calculate change dynamically
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
                val changeText = tvKembalian?.text ?: formatCurrency(cashAmount - totalAmount)
                Toast.makeText(this, "Pembayaran Berhasil! Kembalian: $changeText", Toast.LENGTH_LONG).show()
                // Navigate to Home
                val intent = Intent(this, ActivityHome::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Uang tunai tidak cukup.", Toast.LENGTH_SHORT).show()
            }
        }
        
        setupBottomNav()
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
            // Remove Rp, dots, spaces, and potential "Run" typo
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
            val intent = Intent(this, ActivityHome::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }
    }
}
