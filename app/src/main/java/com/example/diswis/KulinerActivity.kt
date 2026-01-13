package com.example.diswis

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class KulinerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kuliner)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, Fragment_Kuliner())
                .commit()
        }
    }
}
