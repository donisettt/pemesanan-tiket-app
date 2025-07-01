package com.donisw.pemesanantiket.database

import androidx.room.Database
import com.donisw.pemesanantiket.model.ModelDatabase
import androidx.room.RoomDatabase
import com.donisw.pemesanantiket.database.dao.DatabaseDao

@Database(entities = [ModelDatabase::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun databaseDao(): DatabaseDao?
}