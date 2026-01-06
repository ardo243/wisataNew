package com.example.diswis

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.diswis.utils.SessionManager

class ActivityHome : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sessionManager = SessionManager(this)

        // ✅ CEK SESSION DULU (PALING PENTING)
        if (!sessionManager.isLoggedIn()) {
            startActivity(Intent(this, ActivityLogin::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_home)

        // Handle edge-to-edge padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_home)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // ===== TAMPILKAN DATA USER =====
        val tvUsername = findViewById<TextView>(R.id.tv_username)
        tvUsername.text = sessionManager.getEmail() ?: "User"

        // ===== MENU NAVIGASI =====
        findViewById<LinearLayout>(R.id.btn_destinasi).setOnClickListener {
            // startActivity(Intent(this, DestinationActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.btn_paket_wisata).setOnClickListener {
            startActivity(Intent(this, PaketWisataActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.btn_kuliner).setOnClickListener {
            // startActivity(Intent(this, KulinerActivity::class.java))
        }
    }
}
