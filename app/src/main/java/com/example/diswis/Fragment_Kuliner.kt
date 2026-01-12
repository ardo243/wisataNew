package com.example.diswis

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.diswis.api.ApiClient
import com.example.diswis.response.kuliner.Data
import com.example.diswis.response.kuliner.ResponKuliner
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Fragment_Kuliner : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdapterKuliner
    private val kulinerList = ArrayList<Data>() // Displayed List
    private val fullList = ArrayList<Data>()      // Original Data Backup

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Use fragment__kuliner (two underscores as per file name found)
        val view = inflater.inflate(R.layout.fragment__kuliner, container, false)

        val btnBack = view.findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener {
            if (parentFragmentManager.backStackEntryCount > 0) {
                parentFragmentManager.popBackStack()
            } else {
                activity?.finish()
            }
        }

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.rvKuliner)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = AdapterKuliner(kulinerList)
        recyclerView.adapter = adapter
        
        // Fetch Data from API
        fetchKuliner()

        // Search Functionality
        val etSearch = view.findViewById<EditText>(R.id.et_search_kuliner)
        etSearch.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterKuliner(s.toString())
            }
            override fun afterTextChanged(s: android.text.Editable?) {}
        })

        // Bottom Navigation - Wishlist (assuming duplicate logic from Destinasi)
        val navFav = view.findViewById<ImageView>(R.id.nav_fav)
        navFav.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, Fragment_wishlist())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        return view
    }

    private fun fetchKuliner() {
        ApiClient.instance.getKuliner().enqueue(object : Callback<ResponKuliner> {
            override fun onResponse(call: Call<ResponKuliner>, response: Response<ResponKuliner>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.status == true) {
                        // responseBody.data might be null safe check
                        val data = responseBody.data
                        if (data != null) {
                            kulinerList.clear()
                            fullList.clear()
                            
                            kulinerList.addAll(data)
                            fullList.addAll(data)
                            
                            adapter.notifyDataSetChanged()
                        } else {
                            Toast.makeText(context, "Data kosong", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Data tidak ditemukan", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Gagal: ${response.code()} ${response.message()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ResponKuliner>, t: Throwable) {
                Log.e("Fragment_Kuliner", "onFailure: ${t.message}")
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun filterKuliner(query: String) {
        val filteredList = if (query.isEmpty()) {
            fullList
        } else {
            fullList.filter {
                it.namaTempat?.contains(query, ignoreCase = true) == true
            }
        }
        adapter.updateData(filteredList)
    }
}