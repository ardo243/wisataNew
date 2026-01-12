package com.example.diswis

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.diswis.api.ApiClient
import com.example.diswis.response.destinasi.Data
import com.example.diswis.response.destinasi.Destinasi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DestinasiFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdapterDestinasi
    private val destinationList = ArrayList<Data>() // Displayed list
    private val fullList = ArrayList<Data>()      // Original Data Backup

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_destinasi, container, false)

        val btnBack = view.findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener {
            if (parentFragmentManager.backStackEntryCount > 0) {
                parentFragmentManager.popBackStack()
            } else {
                activity?.finish()
            }
        }

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.rvDestinasi)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = AdapterDestinasi(destinationList)
        recyclerView.adapter = adapter
        
        // Fetch Data from API
        fetchDestinations()

        // Search Functionality
        val etSearch = view.findViewById<android.widget.EditText>(R.id.et_search_destinasi)
        etSearch.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterDestinations(s.toString())
            }
            override fun afterTextChanged(s: android.text.Editable?) {}
        })

        // Bottom Navigation - Wishlist
        val navFav = view.findViewById<ImageView>(R.id.nav_fav)
        navFav.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, Fragment_wishlist())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        return view
    }

    private fun fetchDestinations() {
        ApiClient.instance.getDestinasi().enqueue(object : Callback<Destinasi> {
            override fun onResponse(call: Call<Destinasi>, response: Response<Destinasi>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.status) {
                        destinationList.clear()
                        fullList.clear()
                        
                        destinationList.addAll(responseBody.dataList)
                        fullList.addAll(responseBody.dataList)
                        
                        adapter.notifyDataSetChanged()
                        // Toast.makeText(context, "Data: ${responseBody.dataList.size} item", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Data tidak ditemukan", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Gagal: ${response.code()} ${response.message()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Destinasi>, t: Throwable) {
                Log.e("DestinasiFragment", "onFailure: ${t.message}")
                // Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun filterDestinations(query: String) {
        val filteredList = if (query.isEmpty()) {
            fullList
        } else {
            fullList.filter {
                it.nama_wisata?.contains(query, ignoreCase = true) == true
            }
        }
        adapter.updateData(filteredList)
    }
}
