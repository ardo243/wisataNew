package com.example.diswis

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.diswis.response.kuliner.Data
import com.squareup.picasso.Picasso

class AdapterKuliner(private val listKuliner: ArrayList<Data>) :
    RecyclerView.Adapter<AdapterKuliner.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivKuliner: ImageView = itemView.findViewById(R.id.ivKuliner)
        val ivWishlist: ImageView = itemView.findViewById(R.id.iv_wishlist) // Bind Love Icon
        val tvKulinerName: TextView = itemView.findViewById(R.id.tvKulinerName)
        val tvKulinerLocation: TextView = itemView.findViewById(R.id.tvKulinerLocation)
        val tvKulinerJamOp: TextView = itemView.findViewById(R.id.tvKulinerhrg)

        fun bind(data: Data) {
            // Set Text
            tvKulinerName.text = data.namaTempat
            tvKulinerLocation.text = data.lokasi
            tvKulinerJamOp.text = "Buka: ${data.jamOp}"

            // Set Image
            // Base URL for images
            val imgBaseUrl = "http://10.0.2.2/api_wisata/gambar/"
            Picasso.get()
                .load(imgBaseUrl + data.gambar)
                .placeholder(android.R.drawable.ic_menu_gallery) // Generic placeholder
                .error(android.R.color.darker_gray)
                .into(ivKuliner)

            // WISHLIST LOGIC (Convert Kuliner Data -> Destinasi Data for WishlistManager)
             val context = itemView.context
             
             // Create a Destinasi Data object from Kuliner Data
             // Note: using jamOp as jam_op, and null for harga_masuk/deskripsi if needed
             val destinasiData = com.example.diswis.response.destinasi.Data(
                id_wisata = data.idKuliner?.toString(),
                nama_wisata = data.namaTempat,
                lokasi = data.lokasi,
                deskripsi = data.deskripsi,
                harga_masuk = "0", // Default or N/A
                jam_op = data.jamOp,
                gambar = data.gambar
             )

            val isFavorite = WishlistManager.isFavorite(destinasiData.nama_wisata ?: "")
            if (isFavorite) {
                ivWishlist.setColorFilter(android.graphics.Color.RED)
            } else {
                 ivWishlist.setColorFilter(android.graphics.Color.WHITE)
            }
    
            ivWishlist.setOnClickListener {
                if (WishlistManager.isFavorite(destinasiData.nama_wisata ?: "")) {
                    WishlistManager.remove(destinasiData)
                    ivWishlist.setColorFilter(android.graphics.Color.WHITE)
                    android.widget.Toast.makeText(context, "Dihapus dari Wishlist", android.widget.Toast.LENGTH_SHORT).show()
                } else {
                    WishlistManager.add(destinasiData)
                    ivWishlist.setColorFilter(android.graphics.Color.RED)
                    android.widget.Toast.makeText(context, "Ditambahkan ke Wishlist", android.widget.Toast.LENGTH_SHORT).show()
                }
            }

            // Fitur Klik: Pindah ke Activity_detai_kuliner
            itemView.setOnClickListener {
                // Pastikan kamu punya Activity_detai_kuliner atau sesuaikan tujuannya
                val intent = Intent(itemView.context, Activity_detai_kuliner::class.java)
                intent.putExtra("id_kuliner", data.idKuliner)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_item_kuliner, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listKuliner[position])
    }

    override fun getItemCount(): Int = listKuliner.size

    // Fungsi untuk update data dari API
    fun updateData(newList: List<Data>) {
        listKuliner.clear()
        listKuliner.addAll(newList)
        notifyDataSetChanged()
    }
}
