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
            // v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)

            val bottomNav = findViewById<LinearLayout>(R.id.bottomNav)
            bottomNav.setPadding(0, 0, 0, systemBars.bottom)
            
            insets
        }

        // ===== TAMPILKAN DATA USER =====
        val tvUsername = findViewById<TextView>(R.id.tv_username)
        tvUsername.text = sessionManager.getEmail() ?: "User"

        // ===== MENU NAVIGASI =====
        findViewById<LinearLayout>(R.id.btn_destinasi).setOnClickListener {
            startActivity(Intent(this, DestinationActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.btn_paket_wisata).setOnClickListener {
            startActivity(Intent(this, PaketWisataActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.btn_kuliner).setOnClickListener {
            startActivity(Intent(this, KulinerActivity::class.java))
        }

        // ===== SEARCH FUNCTIONALITY =====
        val etSearch = findViewById<android.widget.EditText>(R.id.et_search)
        etSearch.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()
                val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as? TopDestinationsFragment
                fragment?.filterDestinations(query)
            }

            override fun afterTextChanged(s: android.text.Editable?) {}
        })

        // NOTE: Bottom Navigation IDs might have changed in XML.
        // If Logic for bottom nav exists elsewhere or is just icons, it is fine.
        // Logic for profile icon in bottom nav:
        findViewById<android.widget.ImageView>(R.id.nav_profile).setOnClickListener {
             // startActivity(Intent(this, ProfileActivity::class.java)) 
        }
    }
}
