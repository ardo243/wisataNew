package com.example.diswis

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class PaketWisataActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paket_wisata)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, Fragment_PaketWisata())
                .commit()
        }
    }
}
