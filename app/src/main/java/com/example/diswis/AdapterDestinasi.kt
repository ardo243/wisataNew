package com.example.diswis

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.diswis.response.destinasi.Data
import com.squareup.picasso.Picasso

class AdapterDestinasi(private val listDestinasi: ArrayList<Data>) :
    RecyclerView.Adapter<AdapterDestinasi.ViewHolder>() {

    // 1. ViewHolder untuk menghubungkan ID di XML
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivDestination: ImageView = itemView.findViewById(R.id.ivDestination)
        private val tvDestinationName: TextView = itemView.findViewById(R.id.tvDestinationName)
        private val tvDestinationLocation: TextView = itemView.findViewById(R.id.tvDestinationLocation)
        private val tvDestinationPrice: TextView = itemView.findViewById(R.id.tvDestinationPrice)

        fun bind(data: Data) {
            // Set Teks
            tvDestinationName.text = data.nama_wisata
            tvDestinationLocation.text = data.lokasi
            tvDestinationPrice.text = "Rp ${data.harga_masuk}"

            // Set Gambar menggunakan Picasso
            val imgBaseUrl = "http://10.0.2.2/api_wisata/gambar/"
            Picasso.get()
                .load(imgBaseUrl + data.gambar)
                .placeholder(R.drawable.candi_prambanan)
                .error(android.R.color.darker_gray)
                .into(ivDestination)

            // FITUR KLIK: Berpindah ke DetailDestinasiActivity
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailDestinasiActivity::class.java)
                // Pastikan key "id_wisata" sama dengan yang ditangkap di DetailDestinasiActivity
                intent.putExtra("id_wisata", data.id_wisata)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_item_destinasi, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listDestinasi[position])
    }

    override fun getItemCount(): Int = listDestinasi.size

    // Fungsi untuk memperbarui data dari API
    fun updateData(newList: List<Data>) {
        listDestinasi.clear()
        listDestinasi.addAll(newList)
        notifyDataSetChanged()
    }
}
