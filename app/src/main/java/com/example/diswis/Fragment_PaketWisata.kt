package com.example.diswis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.diswis.R
import com.example.diswis.AdapterPaket
import com.example.diswis.api.ApiClient
import com.example.diswis.response.paket.PaketRespon
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Fragment_PaketWisata: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_paket_wisata, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_paket_wisata)
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)

        ApiClient.instance.getPaket()
            .enqueue(object : Callback<PaketRespon> {

                override fun onResponse(
                    call: Call<PaketRespon>,
                    response: Response<PaketRespon>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()?.data ?: emptyList()
                        recyclerView.adapter = AdapterPaket(data) {
                            val intent = android.content.Intent(requireContext(), AturJadwalActivity::class.java)
                            intent.putExtra("EXTRA_TITLE", it.namaPaket)
                            intent.putExtra("EXTRA_PRICE", it.harga)
                            val idToSend = it.idPaket ?: it.idWisata
                            intent.putExtra("EXTRA_ID_WISATA", idToSend)
                            startActivity(intent)
                        }
                    }
                }
                override fun onFailure(call: Call<PaketRespon>, t: Throwable) {
                    Toast.makeText(
                        requireContext(),
                        "Gagal memuat data",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}
