package com.example.diswis

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.diswis.response.destinasi.Data

class AdapterTopDestinations(private var listDestinasi: ArrayList<Data>) :
    RecyclerView.Adapter<AdapterTopDestinations.ViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Data)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivDestination: ImageView = itemView.findViewById(R.id.ivDestination)
        var ivWishlist: ImageView = itemView.findViewById(R.id.iv_wishlist) // Bind Love Icon
        var tvDestinationName: TextView = itemView.findViewById(R.id.tvDestinationName)
        var tvDestinationLocation: TextView = itemView.findViewById(R.id.tvDestinationLocation)
        var tvDestinationPrice: TextView = itemView.findViewById(R.id.tvDestinationPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_destinasi, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val destination = listDestinasi[position]

        holder.tvDestinationName.text = destination.nama_wisata ?: "Nama tidak tersedia"
        holder.tvDestinationLocation.text = destination.lokasi ?: "Lokasi tidak tersedia"
        holder.tvDestinationPrice.text = destination.harga_masuk ?: "Gratis"

        Glide.with(holder.itemView.context)
            .load("http://10.0.2.2/api_wisata/gambar/" + destination.gambar)
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.stat_notify_error)
            .into(holder.ivDestination)

        // WISHLIST LOGIC
        val context = holder.itemView.context
        
        // Check initial state
        val isFavorite = WishlistManager.isFavorite(destination.nama_wisata ?: "")
        if (isFavorite) {
            holder.ivWishlist.setColorFilter(android.graphics.Color.RED)
        } else {
             holder.ivWishlist.setColorFilter(android.graphics.Color.WHITE)
        }

        holder.ivWishlist.setOnClickListener {
            if (WishlistManager.isFavorite(destination.nama_wisata ?: "")) {
                WishlistManager.remove(destination)
                holder.ivWishlist.setColorFilter(android.graphics.Color.WHITE)
                android.widget.Toast.makeText(context, "Dihapus dari Wishlist", android.widget.Toast.LENGTH_SHORT).show()
            } else {
                WishlistManager.add(destination)
                holder.ivWishlist.setColorFilter(android.graphics.Color.RED)
                android.widget.Toast.makeText(context, "Ditambahkan ke Wishlist", android.widget.Toast.LENGTH_SHORT).show()
            }
        }

        holder.itemView.setOnClickListener {
            onItemClickCallback?.onItemClicked(listDestinasi[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int = listDestinasi.size

    fun updateData(newList: List<Data>) {
        listDestinasi.clear()
        listDestinasi.addAll(newList)
        notifyDataSetChanged()
    }
}
