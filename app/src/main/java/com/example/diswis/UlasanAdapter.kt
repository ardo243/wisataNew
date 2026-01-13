package com.example.diswis

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.diswis.response.ulasan.Data

class UlasanAdapter(private val listUlasan: List<Data>) : RecyclerView.Adapter<UlasanAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvUsername: TextView = itemView.findViewById(R.id.tv_username)
        val tvTanggal: TextView = itemView.findViewById(R.id.tv_tanggal)
        val tvKomentar: TextView = itemView.findViewById(R.id.tv_komentar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ulasan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ulasan = listUlasan[position]

        // Placeholder for username since it's not in the data model yet
        holder.tvUsername.text = ulasan.username ?: "Pengunjung"
        holder.tvTanggal.text = ulasan.createdAt
        holder.tvKomentar.text = ulasan.komentar
    }

    override fun getItemCount(): Int {
        return listUlasan.size
    }
}
