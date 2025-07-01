package com.donisw.pemesanantiket.view.auth

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.donisw.pemesanantiket.R
import com.donisw.pemesanantiket.database.DatabaseClient
import com.donisw.pemesanantiket.model.UserEntity

class RegisterActivity : AppCompatActivity() {

    private lateinit var etNamaLengkap: EditText
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var etNoHp: EditText
    private lateinit var etEmail: EditText
    private lateinit var btnDaftar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etNamaLengkap = findViewById(R.id.etNamaLengkap)
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        etNoHp = findViewById(R.id.etNoHp)
        etEmail = findViewById(R.id.etEmail)
        btnDaftar = findViewById(R.id.btnDaftar)

        btnDaftar.setOnClickListener {
            val nama = etNamaLengkap.text.toString()
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()
            val noHp = etNoHp.text.toString()
            val email = etEmail.text.toString()

            if (nama.isEmpty() || username.isEmpty() || password.isEmpty() || noHp.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Semua field harus diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = UserEntity(0, nama, username, password, noHp, email)

            Thread {
                DatabaseClient.getInstance(applicationContext)
                    .appDatabase
                    .databaseDao()
                    ?.insertUser(user)

                runOnUiThread {
                    Toast.makeText(this, "Registrasi berhasil!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }.start()
        }
    }
}