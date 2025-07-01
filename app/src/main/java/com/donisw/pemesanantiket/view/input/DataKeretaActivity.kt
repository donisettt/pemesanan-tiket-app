package com.donisw.pemesanantiket.view.input

import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.donisw.pemesanantiket.R
import com.donisw.pemesanantiket.viewmodel.InputDataViewModel
import java.text.SimpleDateFormat
import java.util.*

class DataKeretaActivity : AppCompatActivity() {

    // View
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var inputNama: EditText
    private lateinit var inputTelp: EditText
    private lateinit var inputTanggal: EditText
    private lateinit var spBerangkat: Spinner
    private lateinit var spTujuan: Spinner
    private lateinit var spKelas: Spinner
    private lateinit var imageAdd1: ImageView
    private lateinit var imageMinus1: ImageView
    private lateinit var imageAdd2: ImageView
    private lateinit var imageMinus2: ImageView
    private lateinit var tvJmlAnak: TextView
    private lateinit var tvJmlDewasa: TextView
    private lateinit var btnCheckout: Button

    // Data
    private val strAsal = arrayOf("Jakarta", "Semarang", "Surabaya", "Bali")
    private val strTujuan = arrayOf("Jakarta", "Semarang", "Surabaya", "Bali")
    private val strKelas = arrayOf("Eksekutif", "Bisnis", "Ekonomi")
    private lateinit var inputDataViewModel: InputDataViewModel
    private lateinit var sAsal: String
    private lateinit var sTujuan: String
    private lateinit var sTanggal: String
    private lateinit var sNama: String
    private lateinit var sTelp: String
    private lateinit var sKelas: String
    private var hargaDewasa = 0
    private var hargaAnak = 0
    private var hargaKelas = 0
    private var hargaTotalDewasa = 0
    private var hargaTotalAnak = 0
    private var hargaTotal = 0
    private var jmlDewasa = 0
    private var jmlAnak = 0
    private var countAnak = 0
    private var countDewasa = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_data)

        initViews()
        setStatusBar()
        setToolbar()
        setViewModel()
        setSpinnerAdapter()
        setJmlPenumpang()
        setInputData()
    }

    private fun initViews() {
        toolbar = findViewById(R.id.toolbar)
        inputNama = findViewById(R.id.inputNama)
        inputTelp = findViewById(R.id.inputTelepon)
        inputTanggal = findViewById(R.id.inputTanggal)
        spBerangkat = findViewById(R.id.spBerangkat)
        spTujuan = findViewById(R.id.spTujuan)
        spKelas = findViewById(R.id.spKelas)
        imageAdd1 = findViewById(R.id.imageAdd1)
        imageMinus1 = findViewById(R.id.imageMinus1)
        imageAdd2 = findViewById(R.id.imageAdd2)
        imageMinus2 = findViewById(R.id.imageMinus2)
        tvJmlAnak = findViewById(R.id.tvJmlAnak)
        tvJmlDewasa = findViewById(R.id.tvJmlDewasa)
        btnCheckout = findViewById(R.id.btnCheckout)

        inputTanggal.setOnClickListener {
            val tanggalJemput = Calendar.getInstance()
            val date = OnDateSetListener { _, year, month, day ->
                tanggalJemput.set(year, month, day)
                val format = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
                inputTanggal.setText(format.format(tanggalJemput.time))
            }
            DatePickerDialog(
                this, date,
                tanggalJemput.get(Calendar.YEAR),
                tanggalJemput.get(Calendar.MONTH),
                tanggalJemput.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun setViewModel() {
        inputDataViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(InputDataViewModel::class.java)
    }

    private fun setToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun setSpinnerAdapter() {
        spBerangkat.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, strAsal)
        spTujuan.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, strTujuan)
        spKelas.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, strKelas)

        spBerangkat.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                sAsal = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        spTujuan.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                sTujuan = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        spKelas.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                sKelas = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setJmlPenumpang() {
        imageAdd1.setOnClickListener {
            countAnak++
            tvJmlAnak.text = countAnak.toString()
        }

        imageMinus1.setOnClickListener {
            if (countAnak > 0) {
                countAnak--
                tvJmlAnak.text = countAnak.toString()
            }
        }

        imageAdd2.setOnClickListener {
            countDewasa++
            tvJmlDewasa.text = countDewasa.toString()
        }

        imageMinus2.setOnClickListener {
            if (countDewasa > 0) {
                countDewasa--
                tvJmlDewasa.text = countDewasa.toString()
            }
        }
    }

    private fun setInputData() {
        btnCheckout.setOnClickListener {
            setPerhitunganHargaTiket()
            sNama = inputNama.text.toString()
            sTelp = inputTelp.text.toString()
            sTanggal = inputTanggal.text.toString()

            if (sNama.isEmpty() || sTelp.isEmpty() || sTanggal.isEmpty()
                || sAsal.isEmpty() || sTujuan.isEmpty()
                || countDewasa == 0 || sKelas.isEmpty()
            ) {
                Toast.makeText(this, "Mohon lengkapi data pemesanan!", Toast.LENGTH_SHORT).show()
            } else if (sAsal == sTujuan) {
                Toast.makeText(this, "Asal dan Tujuan tidak boleh sama!", Toast.LENGTH_LONG).show()
            } else {
                inputDataViewModel.addDataPemesan(
                    sNama, sAsal, sTujuan, sTanggal, sTelp,
                    countAnak, countDewasa, hargaTotal, sKelas, "1"
                )
                Toast.makeText(this, "Booking Tiket berhasil, cek di menu riwayat", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun setPerhitunganHargaTiket() {
        hargaDewasa = when {
            sAsal == "Jakarta" && sTujuan == "Semarang" -> 200000
            sAsal == "Jakarta" && sTujuan == "Surabaya" -> 500000
            sAsal == "Jakarta" && sTujuan == "Bali" -> 800000
            sAsal == "Semarang" && sTujuan == "Jakarta" -> 200000
            sAsal == "Semarang" && sTujuan == "Surabaya" -> 300000
            sAsal == "Semarang" && sTujuan == "Bali" -> 500000
            sAsal == "Surabaya" && sTujuan == "Jakarta" -> 500000
            sAsal == "Surabaya" && sTujuan == "Semarang" -> 300000
            sAsal == "Surabaya" && sTujuan == "Bali" -> 300000
            sAsal == "Bali" && sTujuan == "Jakarta" -> 800000
            sAsal == "Bali" && sTujuan == "Semarang" -> 500000
            sAsal == "Bali" && sTujuan == "Surabaya" -> 300000
            else -> 0
        }

        hargaAnak = when {
            hargaDewasa == 800000 -> 60000
            hargaDewasa == 500000 -> 40000
            hargaDewasa == 300000 -> 20000
            hargaDewasa == 200000 -> 20000
            else -> 0
        }

        hargaKelas = when (sKelas) {
            "Eksekutif" -> 200000
            "Bisnis" -> 100000
            "Ekonomi" -> 80000
            else -> 0
        }

        jmlDewasa = countDewasa
        jmlAnak = countAnak
        hargaTotalDewasa = jmlDewasa * hargaDewasa
        hargaTotalAnak = jmlAnak * hargaAnak
        hargaTotal = hargaTotalDewasa + hargaTotalAnak + hargaKelas
    }

    private fun setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
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