package com.donisw.pemesanantiket.view.input

import android.app.Activity
import android.app.DatePickerDialog
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

class DataPesawatActivity : AppCompatActivity() {

    val strAsal = arrayOf("Jakarta", "Semarang", "Surabaya", "Bali")
    val strTujuan = arrayOf("Jakarta", "Semarang", "Surabaya", "Bali")
    val strKelas = arrayOf("Eksekutif", "Bisnis", "Ekonomi")

    lateinit var inputDataViewModel: InputDataViewModel
    lateinit var sAsal: String
    lateinit var sTujuan: String
    lateinit var sTanggal: String
    lateinit var sNama: String
    lateinit var sTelp: String
    lateinit var sKelas: String

    var hargaDewasa = 0
    var hargaAnak = 0
    var hargaKelas = 0
    var hargaTotalDewasa = 0
    var hargaTotalAnak = 0
    var hargaTotal = 0
    var jmlDewasa = 0
    var jmlAnak = 0
    var countAnak = 0
    var countDewasa = 0

    // Views
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var inputTanggal: EditText
    lateinit var inputNama: EditText
    lateinit var inputTelp: EditText
    lateinit var spBerangkat: Spinner
    lateinit var spTujuan: Spinner
    lateinit var spKelas: Spinner
    lateinit var tvJmlAnak: TextView
    lateinit var tvJmlDewasa: TextView
    lateinit var imageAdd1: ImageView
    lateinit var imageMinus1: ImageView
    lateinit var imageAdd2: ImageView
    lateinit var imageMinus2: ImageView
    lateinit var btnCheckout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_data)

        initViews()
        setStatusBar()
        setToolbar()
        setInitView()
        setViewModel()
        setSpinnerAdapter()
        setJmlPenumpang()
        setInputData()
    }

    private fun initViews() {
        toolbar = findViewById(R.id.toolbar)
        inputTanggal = findViewById(R.id.inputTanggal)
        inputNama = findViewById(R.id.inputNama)
        inputTelp = findViewById(R.id.inputTelepon)
        spBerangkat = findViewById(R.id.spBerangkat)
        spTujuan = findViewById(R.id.spTujuan)
        spKelas = findViewById(R.id.spKelas)
        tvJmlAnak = findViewById(R.id.tvJmlAnak)
        tvJmlDewasa = findViewById(R.id.tvJmlDewasa)
        imageAdd1 = findViewById(R.id.imageAdd1)
        imageMinus1 = findViewById(R.id.imageMinus1)
        imageAdd2 = findViewById(R.id.imageAdd2)
        imageMinus2 = findViewById(R.id.imageMinus2)
        btnCheckout = findViewById(R.id.btnCheckout)
    }

    private fun setViewModel() {
        inputDataViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)
        ).get(InputDataViewModel::class.java)
    }

    private fun setToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun setInitView() {
        inputTanggal.setOnClickListener {
            val tanggalJemput = Calendar.getInstance()
            val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
                tanggalJemput.set(year, month, day)
                val format = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
                inputTanggal.setText(format.format(tanggalJemput.time))
            }
            DatePickerDialog(
                this, dateSetListener,
                tanggalJemput.get(Calendar.YEAR),
                tanggalJemput.get(Calendar.MONTH),
                tanggalJemput.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun setSpinnerAdapter() {
        spBerangkat.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, strAsal).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spTujuan.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, strTujuan).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spKelas.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, strKelas).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        spBerangkat.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p: AdapterView<*>, v: View?, pos: Int, id: Long) {
                sAsal = p.getItemAtPosition(pos).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        spTujuan.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p: AdapterView<*>, v: View?, pos: Int, id: Long) {
                sTujuan = p.getItemAtPosition(pos).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        spKelas.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p: AdapterView<*>, v: View?, pos: Int, id: Long) {
                sKelas = p.getItemAtPosition(pos).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
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
            sTanggal = inputTanggal.text.toString()
            sTelp = inputTelp.text.toString()

            if (sNama.isEmpty() || sTanggal.isEmpty() || sTelp.isEmpty()
                || sAsal.isEmpty() || sTujuan.isEmpty() || countDewasa == 0
                || hargaTotal == 0 || sKelas.isEmpty()
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
        when {
            sAsal == "Jakarta" && sTujuan == "Semarang" -> {
                hargaDewasa = 1200000
                hargaAnak = 120000
            }
            sAsal == "Jakarta" && sTujuan == "Surabaya" -> {
                hargaDewasa = 1500000
                hargaAnak = 140000
            }
            sAsal == "Jakarta" && sTujuan == "Bali" -> {
                hargaDewasa = 1800000
                hargaAnak = 160000
            }
            // tambahkan semua kondisi lainnya sesuai kebutuhan...
        }

        hargaKelas = when (sKelas.lowercase()) {
            "eksekutif", "bisnis" -> 500000
            "ekonomi" -> 100000
            else -> 0
        }

        hargaTotalDewasa = countDewasa * hargaDewasa
        hargaTotalAnak = countAnak * hargaAnak
        hargaTotal = hargaTotalDewasa + hargaTotalAnak + hargaKelas
    }

    private fun setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
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
            if (on) layoutParams.flags = layoutParams.flags or bits
            else layoutParams.flags = layoutParams.flags and bits.inv()
            window.attributes = layoutParams
        }
    }
}