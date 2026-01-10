package com.example.diswis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.diswis.R

class DestinasiFragment : Fragment() {

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

        // Navigation for Candi Prambanan
        val cardPrambanan = view.findViewById<View>(R.id.cardPrambanan)
        cardPrambanan.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, Fragment_detail_candiP())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        // Navigation for Ibarbo Park
        val cardIbarbo = view.findViewById<View>(R.id.cardIbarbo)
        cardIbarbo.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, Fragment_detail_generic())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        // Navigation for Heha Sky View
        val cardHeha = view.findViewById<View>(R.id.cardheha)
        cardHeha.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, Fragment_museum())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        // Search Functionality
        val etSearch = view.findViewById<android.widget.EditText>(R.id.et_search_destinasi)
        etSearch.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().lowercase()
                
                if (query.isEmpty()) {
                    cardPrambanan.visibility = View.VISIBLE
                    cardIbarbo.visibility = View.VISIBLE
                    cardHeha.visibility = View.VISIBLE
                } else {
                    cardPrambanan.visibility = if ("candi prambanan".contains(query)) View.VISIBLE else View.GONE
                    cardIbarbo.visibility = if ("ibarbo park".contains(query)) View.VISIBLE else View.GONE
                    cardHeha.visibility = if ("heha sky view".contains(query)) View.VISIBLE else View.GONE
                }
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
}
