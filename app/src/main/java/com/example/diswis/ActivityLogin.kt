package com.example.diswis

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.diswis.api.ApiClient
import com.example.diswis.api.models.LoginResponse
import com.example.diswis.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityLogin : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sessionManager = SessionManager(this)

        // ✅ AUTO LOGIN
        if (sessionManager.isLoggedIn()) {
            startActivity(Intent(this, ActivityHome::class.java))
            finish()
            return
        }

        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        val btnLogin = findViewById<Button>(R.id.btn_login)
        val emailInput = findViewById<EditText>(R.id.username_input)
        val passwordInput = findViewById<EditText>(R.id.password_input)

        btnLogin.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email dan Password harus diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            ApiClient.instance.login(email, password)
                .enqueue(object : Callback<LoginResponse> {

                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        if (!response.isSuccessful) {
                            Toast.makeText(
                                this@ActivityLogin,
                                "Login gagal (Server ${response.code()})",
                                Toast.LENGTH_LONG
                            ).show()
                            return
                        }

                        val loginResponse = response.body()

                        // ✅ VALIDASI LENGKAP
                        if (loginResponse != null &&
                            loginResponse.status &&
                            loginResponse.data.logged_in
                        ) {

                            // ✅ SIMPAN SESSION DARI DATA API
                            sessionManager.createLoginSession(
                                email = loginResponse.data.email,
                                nama = loginResponse.data.username
                            )

                            Toast.makeText(
                                this@ActivityLogin,
                                "Login Berhasil",
                                Toast.LENGTH_SHORT
                            ).show()

                            val intent =
                                Intent(this@ActivityLogin, ActivityHome::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()

                        } else {
                            Toast.makeText(
                                this@ActivityLogin,
                                loginResponse?.message ?: "Login gagal",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(
                            this@ActivityLogin,
                            "Koneksi gagal: ${t.message}",
                            Toast.LENGTH_LONG
                        ).show()
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
