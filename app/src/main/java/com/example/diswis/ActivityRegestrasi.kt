package com.example.diswis

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import android.widget.Toast
import com.example.diswis.api.ApiClient
import com.example.diswis.api.models.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityRegestrasi : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_regestrasi)

        val usernameInput = findViewById<android.widget.EditText>(R.id.username_input)
        val emailInput = findViewById<android.widget.EditText>(R.id.email_input)
        val phoneInput = findViewById<android.widget.EditText>(R.id.phone_input)
        val passwordInput = findViewById<android.widget.EditText>(R.id.password_input)
        val btnRegister = findViewById<android.widget.Button>(R.id.btn_register)

        btnRegister.setOnClickListener {
            val username = usernameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val phone = phoneInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (username.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Semua filed harus diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Call API
            ApiClient.instance.register(username, email, phone, password)
                .enqueue(object : Callback<RegisterResponse> {
                    override fun onResponse(
                        call: Call<RegisterResponse>,
                        response: Response<RegisterResponse>
                    ) {
                        if (response.isSuccessful) {
                            val registerResponse = response.body()
                            if (registerResponse != null && registerResponse.status) {
                                Toast.makeText(this@ActivityRegestrasi, "Registrasi Berhasil: ${registerResponse.message}", Toast.LENGTH_SHORT).show()
                                finish() // Return to Login
                            } else {
                                Toast.makeText(this@ActivityRegestrasi, "Registrasi Gagal: ${registerResponse?.message}", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this@ActivityRegestrasi, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                        Toast.makeText(this@ActivityRegestrasi, "Koneksi Gagal: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}