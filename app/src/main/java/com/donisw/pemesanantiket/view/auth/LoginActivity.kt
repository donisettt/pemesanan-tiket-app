package com.donisw.pemesanantiket.view.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.donisw.pemesanantiket.R
import com.donisw.pemesanantiket.database.DatabaseClient
import com.donisw.pemesanantiket.view.main.MainActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var etNoHp: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var ivBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etNoHp = findViewById(R.id.etNoHpLogin)
        etPassword = findViewById(R.id.etPasswordLogin)
        btnLogin = findViewById(R.id.btnMasuk)
        ivBack = findViewById(R.id.ivBack)

        btnLogin.setOnClickListener {
            val noHp = etNoHp.text.toString()
            val password = etPassword.text.toString()

            if (noHp.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "No HP dan Password tidak boleh kosong!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Thread {
                val user = DatabaseClient.getInstance(applicationContext)
                    .appDatabase
                    .databaseDao()
                    ?.loginUser(noHp, password)

                runOnUiThread {
                    if (user != null) {
                        val sharedPref = getSharedPreferences("user_session", MODE_PRIVATE)
                        with(sharedPref.edit()) {
                            putBoolean("isLoggedIn", true)
                            putString("user_name", user.nama)
                            apply()
                        }

                        Toast.makeText(this, "Login berhasil!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "No HP atau Password salah!", Toast.LENGTH_SHORT).show()
                    }
                }
            }.start()
        }

        ivBack.setOnClickListener {
            finish()
        }
    }
}