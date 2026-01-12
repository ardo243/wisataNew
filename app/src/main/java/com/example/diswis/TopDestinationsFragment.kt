package com.example.diswis

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

class TopDestinationsFragment : Fragment() {

    private lateinit var adapter: AdapterTopDestinations
    private val listDestinasi = ArrayList<Data>() // Displayed List
    private val fullList = ArrayList<Data>()      // Original Data Backup

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_top_destinations, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvDestinations = view.findViewById<RecyclerView>(R.id.rv_paket_wisata)
        rvDestinations.layoutManager = LinearLayoutManager(context)
        rvDestinations.isNestedScrollingEnabled = false

        adapter = AdapterTopDestinations(listDestinasi)
        rvDestinations.adapter = adapter

        // Setup Click Listener
        adapter.setOnItemClickCallback(object : AdapterTopDestinations.OnItemClickCallback {
            override fun onItemClicked(data: Data) {
                val intent = Intent(requireContext(), DetailDestinasiActivity::class.java)
                intent.putExtra("id_wisata", data.id_wisata) // Pass ID as String
                startActivity(intent)
            }
        })

        fetchDestinations()
    }

    private fun fetchDestinations() {
        ApiClient.instance.getDestinasi().enqueue(object : Callback<Destinasi> {
            override fun onResponse(call: Call<Destinasi>, response: Response<Destinasi>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.status) {
                        listDestinasi.clear()
                        fullList.clear() // Clear backup too
                        
                        listDestinasi.addAll(responseBody.dataList)
                        fullList.addAll(responseBody.dataList) // Populate backup
                        
                        adapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(context, "Data Kosong", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("TopDestinations", "Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Destinasi>, t: Throwable) {
                Log.e("TopDestinations", "Failure: ${t.message}")
                // Optional: Show error toast only if context is valid
                if (isAdded) {
                    Toast.makeText(context, "Gagal memuat data", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
    
     fun filterDestinations(query: String) {
        val filteredList = if (query.isEmpty()) {
            fullList // Restore from backup
        } else {
            fullList.filter { 
                it.nama_wisata?.contains(query, ignoreCase = true) == true
            }
        }
        adapter.updateData(filteredList)
    }
}
