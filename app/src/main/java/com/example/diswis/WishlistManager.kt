package com.example.diswis

import com.example.diswis.response.destinasi.Data

object WishlistManager {
    private val wishlist = ArrayList<Data>()

    fun add(data: Data) {
        if (wishlist.none { it.nama_wisata == data.nama_wisata }) {
            wishlist.add(data)
        }
    }

    fun remove(data: Data) {
        wishlist.removeAll { it.nama_wisata == data.nama_wisata }
    }

    fun getAll(): ArrayList<Data> {
        return wishlist
    }
    
    fun isFavorite(namaWisata: String): Boolean {
        return wishlist.any { it.nama_wisata == namaWisata }
    }
}
