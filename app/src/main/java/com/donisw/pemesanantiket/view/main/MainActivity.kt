package com.donisw.pemesanantiket.view.main

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.donisw.pemesanantiket.R
import com.donisw.pemesanantiket.view.history.HistoryActivity
import com.donisw.pemesanantiket.view.input.DataKapalActivity
import com.donisw.pemesanantiket.view.input.DataKeretaActivity
import com.donisw.pemesanantiket.view.input.DataPesawatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var imageProfile: ImageView
    private lateinit var cvPesawat: CardView
    private lateinit var cvKapal: CardView
    private lateinit var cvKereta: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inisialisasi View
        imageProfile = findViewById(R.id.imageProfile)
        cvPesawat = findViewById(R.id.cvPesawat)
        cvKapal = findViewById(R.id.cvKapal)
        cvKereta = findViewById(R.id.cvKereta)

        setStatusBar()

        imageProfile.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        cvPesawat.setOnClickListener {
            startActivity(Intent(this, DataPesawatActivity::class.java))
        }

        cvKapal.setOnClickListener {
            startActivity(Intent(this, DataKapalActivity::class.java))
        }

        cvKereta.setOnClickListener {
            startActivity(Intent(this, DataKeretaActivity::class.java))
        }
    }

    private fun setStatusBar() {
        if (Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
        }
        if (Build.VERSION.SDK_INT >= 19) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    companion object {
        fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
            val window = activity.window
            val layoutParams = window.attributes
            if (on) {
                layoutParams.flags = layoutParams.flags or bits
            } else {
                layoutParams.flags = layoutParams.flags and bits.inv()
            }
            window.attributes = layoutParams
        }
    }
}