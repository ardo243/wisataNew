package com.example.diswis

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.diswis.response.destinasi.Data

class Fragment_wishlist : Fragment() {

    private lateinit var adapter: AdapterDestinasi
    private lateinit var emptyState: View
    private lateinit var rvWishlist: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_wishlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvWishlist = view.findViewById(R.id.rvWishlist)
        emptyState = view.findViewById(R.id.emptyState)
        val btnBack = view.findViewById<ImageView>(R.id.btnBack)

        rvWishlist.layoutManager = LinearLayoutManager(context)
        adapter = AdapterDestinasi(ArrayList(), isWishlistPage = true)
        rvWishlist.adapter = adapter

        btnBack.setOnClickListener {
             if (parentFragmentManager.backStackEntryCount > 0) {
                 parentFragmentManager.popBackStack()
             } else {
                 activity?.finish()
             }
        }
    }
    
    override fun onResume() {
        super.onResume()
        loadWishlist()
    }

    private fun loadWishlist() {
        val wishlistItems = WishlistManager.getAll()
        if (wishlistItems.isEmpty()) {
            rvWishlist.visibility = View.GONE
            emptyState.visibility = View.VISIBLE
        } else {
            rvWishlist.visibility = View.VISIBLE
            emptyState.visibility = View.GONE
            adapter.updateData(wishlistItems)
        }
    }
}