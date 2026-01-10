package com.example.diswis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TopDestinationsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_top_destinations, container, false)
    }

    private lateinit var adapter: DestinationAdapter
    private val originalDestinations = ArrayList<Destination>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvDestinations = view.findViewById<RecyclerView>(R.id.rv_paket_wisata)
        rvDestinations.layoutManager = LinearLayoutManager(context)
        rvDestinations.isNestedScrollingEnabled = false // Important for nested scrolling in ActivityHome

        // Initialize data
        // Initialize data
        originalDestinations.add(Destination("Candi Prambanan", "Jl. Raya Solo - Yogyakarta No.16, Kranggan, Bokoharjo, Kec. Prambanan, Kabupaten Sleman, Daerah Istimewa Yogyakarta", "Rp 50.000", R.drawable.candi_prambanan))
        originalDestinations.add(Destination("Candi Borobudur", "Jl. Badrawati, Kw. Candi Borobudur, Borobudur, Kec. Borobudur, Kabupaten Magelang, Jawa Tengah", "Rp 75.000", R.drawable.candi_prambanan))
        originalDestinations.add(Destination("HeHa Sky View", "Jl. Dlingo-Patuk No.2, Patuk, Bukit, Kec. Patuk, Kabupaten Gunung Kidul, Daerah Istimewa Yogyakarta", "Rp 20.000", R.drawable.heha))

        // Pass a copy to the adapter initially
        adapter = DestinationAdapter(ArrayList(originalDestinations))
        rvDestinations.adapter = adapter
    }

    fun filterDestinations(query: String) {
        val filteredList = if (query.isEmpty()) {
            originalDestinations
        } else {
            originalDestinations.filter { 
                it.title.contains(query, ignoreCase = true) 
            }
        }
        adapter.updateData(filteredList)
    }
}
