package com.donisw.pemesanantiket.view.history

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.donisw.pemesanantiket.databinding.ActivityHistoryBinding
import com.donisw.pemesanantiket.model.ModelDatabase
import com.donisw.pemesanantiket.viewmodel.HistoryViewModel

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private val modelDatabaseList: MutableList<ModelDatabase> = ArrayList()
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var historyViewModel: HistoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setStatusBar()
        setToolbar()
        setInitLayout()
        setViewModel()
        setUpItemTouchHelper()
    }

    private fun setToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }
    }

    private fun setInitLayout() {
        historyAdapter = HistoryAdapter(modelDatabaseList)
        binding.rvHistory.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@HistoryActivity)
            adapter = historyAdapter
        }
    }

    private fun setViewModel() {
        historyViewModel = ViewModelProvider(this)[HistoryViewModel::class.java]
        historyViewModel.dataList.observe(this) { modelDatabases: List<ModelDatabase> ->
            if (modelDatabases.isEmpty()) {
                binding.tvNotFound.visibility = View.VISIBLE
                binding.rvHistory.visibility = View.GONE
            } else {
                binding.tvNotFound.visibility = View.GONE
                binding.rvHistory.visibility = View.VISIBLE
            }
            historyAdapter.setDataAdapter(modelDatabases)
        }
    }

    private fun setUpItemTouchHelper() {
        val simpleCallback: ItemTouchHelper.SimpleCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.START or ItemTouchHelper.END) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val swipedPosition = viewHolder.adapterPosition
                val modelDatabase = historyAdapter.setSwipeRemove(swipedPosition)
                val uid = modelDatabase.uid
                historyViewModel.deleteDataById(uid)
                Toast.makeText(
                    this@HistoryActivity,
                    "Data yang dipilih sudah dihapus",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        ItemTouchHelper(simpleCallback).attachToRecyclerView(binding.rvHistory)
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
        return if (item.itemId == android.R.id.home) {
            finish()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    companion object {
        fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
            val window = activity.window
            val layoutParams = window.attributes
            layoutParams.flags = if (on) {
                layoutParams.flags or bits
            } else {
                layoutParams.flags and bits.inv()
            }
            window.attributes = layoutParams
        }
    }
}