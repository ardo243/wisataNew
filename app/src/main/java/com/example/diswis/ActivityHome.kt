package com.example.diswis

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.LinearLayout
import android.content.Intent

class ActivityHome : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Setup RecyclerView
        val rvDestinations = findViewById<RecyclerView>(R.id.rv_destinations)
        rvDestinations.layoutManager = LinearLayoutManager(this)
        rvDestinations.isNestedScrollingEnabled = false
        val destinations = listOf(
            Destination("Candi Prambanan", "Rp 50.000", R.drawable.candi_prambanan),
            Destination("Candi Prambanan", "Rp 50.000", R.drawable.candi_prambanan),
            Destination("Tempat Lain", "Rp 75.000", R.drawable.jogja)
        )
        val adapter = DestinationAdapter(destinations)
        rvDestinations.adapter = adapter

        // Setup Category Click (Placeholder)
        val menuDestinasi = findViewById<LinearLayout>(R.id.menu_Distinasi)
        menuDestinasi.setOnClickListener {
            // TODO: Navigate to Destinasi Page
            // val intent = Intent(this, DestinationActivity::class.java)
            // startActivity(intent)
        }

        // Navigation to Profile
        val profileImage = findViewById<android.widget.ImageView>(R.id.profile_image)
        profileImage.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        val navUser = findViewById<android.widget.ImageView>(R.id.nav_user)
        navUser.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }
}