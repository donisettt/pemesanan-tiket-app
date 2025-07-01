package com.donisw.pemesanantiket.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.donisw.pemesanantiket.database.DatabaseClient.Companion.getInstance
import com.donisw.pemesanantiket.database.dao.DatabaseDao
import com.donisw.pemesanantiket.model.ModelDatabase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers

class InputDataViewModel(application: Application) : AndroidViewModel(application) {

    var databaseDao: DatabaseDao? = null

    fun addDataPemesan(
        nama_penumpang: String, keberangkatan: String,
        tujuan: String, tanggal: String, nomor_telepon: String,
        anak_anak: Int, dewasa: Int, harga_tiket: Int, kelas: String, status: String
    ) {
        Completable.fromAction {
            val modelDatabase = ModelDatabase().apply {
                namaPenumpang = nama_penumpang
                this.keberangkatan = keberangkatan
                this.tujuan = tujuan
                this.tanggal = tanggal
                this.nomorTelepon = nomor_telepon
                this.anakAnak = anak_anak
                this.dewasa = dewasa
                this.hargaTiket = harga_tiket
                this.kelas = kelas
                this.status = status
            }
            databaseDao?.insertData(modelDatabase)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }
    init {
        try {
            databaseDao = getInstance(application)?.appDatabase?.databaseDao()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}