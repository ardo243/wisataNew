package com.example.diswis

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.diswis.api.ApiClient
import com.example.diswis.api.models.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityRegestrasi : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            ApiClient.instance.register(
                username = username,
                email = email,
                password = password,
                noTelpon = phone
            ).enqueue(object : Callback<RegisterResponse> {

                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    if (!response.isSuccessful) {
                        Toast.makeText(
                            this@ActivityRegestrasi,
                            "Register gagal (Server error)",
                            Toast.LENGTH_LONG
                        ).show()
                        return
                    }

                    val registerResponse = response.body()

                    if (registerResponse?.status == true) {
                        Toast.makeText(
                            this@ActivityRegestrasi,
                            "Registrasi berhasil, silakan login",
                            Toast.LENGTH_SHORT
                        ).show()

                        startActivity(Intent(this@ActivityRegestrasi, ActivityLogin::class.java))
                        finish()

                    } else {
                        Toast.makeText(
                            this@ActivityRegestrasi,
                            registerResponse?.message ?: "Registrasi gagal",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    Toast.makeText(
                        this@ActivityRegestrasi,
                        "Koneksi gagal: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        }
    }
}
