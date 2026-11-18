package com.veltix.praktikum5

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "mahasiswa.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_MAHASISWA = "mahasiswa"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE $TABLE_MAHASISWA ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nim TEXT, "
                + "nama TEXT, "
                + "alamat TEXT, "
                + "jurusan TEXT, "
                + "nomor_hp TEXT)")
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_MAHASISWA")
        onCreate(db)
    }

    fun getAllMahasiswa(urutNim: Boolean = false): List<Mahasiswa> {
        val list = mutableListOf<Mahasiswa>()
        val orderBy = if (urutNim) "nim ASC" else "nama ASC"
        val cursor = readableDatabase.query(
            TABLE_MAHASISWA, null, null, null, null, null, orderBy
        )
        cursor.use {
            while (it.moveToNext()) list.add(toMahasiswa(it))
        }
        return list
    }

    fun getMahasiswaById(id: Int): Mahasiswa? {
        val cursor = readableDatabase.query(
            TABLE_MAHASISWA, null, "id = ?", arrayOf(id.toString()), null, null, null
        )
        cursor.use {
            if (it.moveToFirst()) return toMahasiswa(it)
        }
        return null
    }

    private fun toMahasiswa(cursor: Cursor) = Mahasiswa(
        id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
        nim = cursor.getString(cursor.getColumnIndexOrThrow("nim")),
        nama = cursor.getString(cursor.getColumnIndexOrThrow("nama")),
        alamat = cursor.getString(cursor.getColumnIndexOrThrow("alamat")),
        jurusan = cursor.getString(cursor.getColumnIndexOrThrow("jurusan")),
        nomorHp = cursor.getString(cursor.getColumnIndexOrThrow("nomor_hp"))
    )

    fun insertMahasiswa(mhs: Mahasiswa): Long {
        val values = ContentValues().apply {
            put("nim", mhs.nim)
            put("nama", mhs.nama)
            put("alamat", mhs.alamat)
            put("jurusan", mhs.jurusan)
            put("nomor_hp", mhs.nomorHp)
        }
        return writableDatabase.insert(TABLE_MAHASISWA, null, values)
    }

    fun updateMahasiswa(mhs: Mahasiswa): Boolean {
        val values = ContentValues().apply {
            put("nim", mhs.nim)
            put("nama", mhs.nama)
            put("alamat", mhs.alamat)
            put("jurusan", mhs.jurusan)
            put("nomor_hp", mhs.nomorHp)
        }
        return writableDatabase.update(TABLE_MAHASISWA, values, "id = ?", arrayOf(mhs.id.toString())) > 0
    }

    fun deleteMahasiswa(id: Int): Int {
        return writableDatabase.delete(TABLE_MAHASISWA, "id = ?", arrayOf(id.toString()))
    }

    fun isNimTerdaftar(nim: String, editId: Int?): Boolean {
        val cursor = readableDatabase.query(
            TABLE_MAHASISWA, arrayOf("id"), "nim = ?", arrayOf(nim), null, null, null
        )
        cursor.use {
            if (it.moveToFirst()) {
                val existingId = it.getInt(it.getColumnIndexOrThrow("id"))
                return editId == null || existingId != editId
            }
        }
        return false
    }
}