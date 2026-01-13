package com.example.diswis

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.diswis.api.ApiClient
import com.example.diswis.response.ulasan.Ulasanpengguna
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UlasanFragment : Fragment(R.layout.fragment_ulasan) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_ulasan)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // sementara hardcode dulu, nanti bisa diambil dari arguments
        val idWisata = 1
        val tipe = "wisata"

        ApiClient.instance.getUlasan(idWisata, tipe)
            .enqueue(object : Callback<Ulasanpengguna> {

                override fun onResponse(
                    call: Call<Ulasanpengguna>,
                    response: Response<Ulasanpengguna>
                ) {
                    if (response.isSuccessful && response.body()?.status == true) {
                        val listUlasan = response.body()!!.listUlasan
                        recyclerView.adapter = UlasanAdapter(listUlasan)
                    } else {
                         Toast.makeText(context, "Gagal memuat ulasan", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Ulasanpengguna>, t: Throwable) {
                    Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
