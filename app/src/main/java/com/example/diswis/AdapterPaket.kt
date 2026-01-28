package com.example.diswis


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.diswis.R
import com.example.diswis.response.paket.Data

class AdapterPaket(
    private val list: List<Data>,
    private val onClick: (Data) -> Unit
) : RecyclerView.Adapter<AdapterPaket.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nama: TextView = view.findViewById(R.id.tv_title)
        val durasi: TextView = view.findViewById(R.id.tv_durationpaket)
        val harga: TextView = view.findViewById(R.id.tv_pricepaket)
        val deskripsi: TextView = view.findViewById(R.id.tv_descriptionpaketwis)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_paket_wisata, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.nama.text = item.namaPaket
        holder.durasi.text = "Durasi: ${item.durasi}"
        holder.harga.text = "Rp ${item.harga}"
        holder.deskripsi.text = item.deskripsi
        val imgBaseUrl = "http://10.0.2.2/api_wisata/gambar/"
        com.squareup.picasso.Picasso.get()
             .load(imgBaseUrl + item.gambar)
             .placeholder(R.drawable.candi_prambanan)
             .error(android.R.color.darker_gray)
             .into(holder.itemView.findViewById<android.widget.ImageView>(R.id.img_package))
        val layoutTags = holder.itemView.findViewById<android.view.ViewGroup>(R.id.layout_tags)
        layoutTags.removeAllViews() // Clear existing tags (from recycling)

        if (!item.fasilitas.isNullOrEmpty()) {
            val tags = item.fasilitas.split(",").map { it.trim() }
            for (tag in tags) {
                if (tag.isNotEmpty()) {
                    val textView = TextView(holder.itemView.context)
                    textView.text = tag.uppercase()
                    textView.setTextColor(android.graphics.Color.parseColor("#616161"))
                    textView.textSize = 10f
                    textView.setTypeface(null, android.graphics.Typeface.BOLD)
                    
                    val params = android.view.ViewGroup.MarginLayoutParams(
                        android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                        android.view.ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    params.marginEnd = 8.dpToPx(holder.itemView.context)
                    textView.layoutParams = params
                    
                    textView.setBackgroundResource(R.drawable.bg_white_rounded_square)
                    textView.backgroundTintList = android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#F5F5F5"))
                    val paddingHorizontal = 12.dpToPx(holder.itemView.context)
                    val paddingVertical = 6.dpToPx(holder.itemView.context)
                    textView.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical)
                    layoutTags.addView(textView)
                }
            }
        }

        holder.itemView.findViewById<android.widget.Button>(R.id.btn_pesan).setOnClickListener {
            onClick(item)
        }
        
        holder.itemView.setOnClickListener {
            onClick(item)
        }
    }
    
    // Extension function for dp to px
    private fun Int.dpToPx(context: android.content.Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }

    override fun getItemCount(): Int = list.size
}