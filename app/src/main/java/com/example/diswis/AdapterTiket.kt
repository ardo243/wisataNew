package com.example.diswis

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.diswis.response.riwayat_transaksi.Data
import java.text.NumberFormat
import java.util.Locale
import com.squareup.picasso.Picasso

class AdapterTiket(private val listTiket: List<Data>) : RecyclerView.Adapter<AdapterTiket.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNamaWisata: TextView = itemView.findViewById(R.id.tv_nama_wisata_tiket)
        val tvTanggal: TextView = itemView.findViewById(R.id.tv_tanggal_wisata_tiket)
        val tvJumlah: TextView = itemView.findViewById(R.id.tv_jumlah_tiket)
        val tvHarga: TextView = itemView.findViewById(R.id.tv_total_harga_tiket)
        // val imgWisata: ImageView = itemView.findViewById(R.id.img_wisata_tiket)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tiket, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tiket = listTiket[position]

        // Nama Wisata (Prioritas: Nama Paket -> Nama Wisata -> Default)
        holder.tvNamaWisata.text = tiket.namaPaket ?: tiket.namaWisata ?: "Tiket Wisata"
        
        // Tanggal
        holder.tvTanggal.text = tiket.tanggalWisata

        // Jumlah Tiket
        holder.tvJumlah.text = "${tiket.jumlahTiket} Tiket"

        // Format Harga
        val harga = tiket.totalHarga.toDoubleOrNull() ?: 0.0
        val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        holder.tvHarga.text = format.format(harga).replace("Rp", "Rp ")
        
        // Image Binding Removed as per design requirement
    }

    override fun getItemCount(): Int {
        return listTiket.size
    }
}
