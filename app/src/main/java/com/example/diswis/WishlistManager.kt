package com.example.diswis

object WishlistManager {
    private val wishlist = ArrayList<Destination>()

    fun add(destination: Destination) {
        if (wishlist.none { it.title == destination.title }) {
            wishlist.add(destination)
        }
    }

    fun remove(destination: Destination) {
        wishlist.removeAll { it.title == destination.title }
    }

    fun getAll(): ArrayList<Destination> {
        return wishlist
    }
    
    fun isFavorite(title: String): Boolean {
        return wishlist.any { it.title == title }
    }
}
