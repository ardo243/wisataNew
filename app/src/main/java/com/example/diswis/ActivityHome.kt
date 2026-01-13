package com.example.diswis

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
// import com.example.diswis.utils.SessionManager
import android.os.Handler
import android.os.Looper
import androidx.viewpager2.widget.ViewPager2
import com.example.diswis.CarouselAdapter
import com.example.diswis.CarouselItem

class ActivityHome : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private var carouselItems: List<CarouselItem> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sessionManager = SessionManager(this)

        // ✅ CEK SESSION DULU (PALING PENTING)
        // Session Check
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

        setupCarousel()

        // ===== TAMPILKAN DATA USER =====
        val tvUsername = findViewById<TextView>(R.id.tv_username)
        val username = sessionManager.getUsername()
        tvUsername.text = username ?: "Pengunjung"

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
             startActivity(Intent(this, ProfileActivity::class.java)) 
        }

        // Ticket Navigation
        findViewById<android.widget.ImageView>(R.id.nav_user).setOnClickListener {
             startActivity(Intent(this, TicketActivity::class.java)) 
        }

        // Wishlist Navigation
        findViewById<android.widget.ImageView>(R.id.nav_fav).setOnClickListener {
            startActivity(Intent(this, WishlistActivity::class.java))
        }
    }

    private fun setupCarousel() {
        viewPager = findViewById(R.id.hero_viewpager)
        val tvTitle = findViewById<TextView>(R.id.tv_hero_title)
        val tvSubtitle = findViewById<TextView>(R.id.tv_hero_subtitle)

        // Data for Carousel
        carouselItems = listOf(
            CarouselItem(
                R.drawable.candi_prambanan,
                "Candi Prambanan",
                "Mahakarya arsitektur Hindu yang memukau dunia"
            ),
            CarouselItem(
                R.drawable.heha,
                "HeHa Ocean View",
                "Nikmati pemandangan laut selatan yang menakjubkan"
            ),
            CarouselItem(
                R.drawable.museum,
                "Museum Ullen Sentalu",
                "Menyelami kekayaan budaya dan sejarah Jawa"
            ),
             CarouselItem(
                R.drawable.jogja,
                "Malioboro",
                "Jantung kota Yogyakarta yang tak pernah tidur"
            )

        )

        val adapter = CarouselAdapter(carouselItems)
        viewPager.adapter = adapter

        // Sync Text with Image
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val item = carouselItems[position]
                tvTitle.text = item.title
                tvSubtitle.text = item.subtitle
            }
        })

        // Auto Slide Logic
        handler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {
            override fun run() {
                val currentItem = viewPager.currentItem
                val nextItem = if (currentItem == carouselItems.size - 1) 0 else currentItem + 1
                viewPager.setCurrentItem(nextItem, true)
                handler.postDelayed(this, 3000) // Slide every 3 seconds
            }
        }
        handler.postDelayed(runnable, 3000)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::handler.isInitialized && ::runnable.isInitialized) {
            handler.removeCallbacks(runnable)
        }
    }

    override fun onPause() {
        super.onPause()
        if (::handler.isInitialized && ::runnable.isInitialized) {
            handler.removeCallbacks(runnable)
        }
    }

    override fun onResume() {
        super.onResume()
        if (::handler.isInitialized && ::runnable.isInitialized) {
            handler.postDelayed(runnable, 3000)
        }
    }
}
