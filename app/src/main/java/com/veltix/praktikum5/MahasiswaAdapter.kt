package com.veltix.praktikum5

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class MahasiswaAdapter(
    private val context: Context,
    private val list: List<Mahasiswa>
) : BaseAdapter() {

    override fun getCount(): Int = list.size
    override fun getItem(position: Int): Any = list[position]
    override fun getItemId(position: Int): Long = list[position].id.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_mahasiswa, parent, false)

        val mhs = list[position]
        view.findViewById<TextView>(R.id.tvNama).text = mhs.nama
        view.findViewById<TextView>(R.id.tvNimJurusan).text = "${mhs.nim} • ${mhs.jurusan}"

        return view
    }
}