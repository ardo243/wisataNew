package com.example.diswis

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment_detail_generic.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment_detail_generic : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_detail_generic, container, false)

        val btnBack = view.findViewById<android.widget.ImageView>(R.id.btnBack)
        btnBack.setOnClickListener {
             if (parentFragmentManager.backStackEntryCount > 0) {
                 parentFragmentManager.popBackStack()
             } else {
                 activity?.finish()
             }
        }

        val btnFavorite = view.findViewById<android.widget.ImageView>(R.id.btnFavorite)
        btnFavorite.setOnClickListener {
            val destination = Destination("Ibarbo Park", "Jl. Magelang No.Km. 14, Sleman", "Rp 35.000", R.drawable.ibarbo)
            WishlistManager.add(destination)
            android.widget.Toast.makeText(context, "Ditambahkan ke Wishlist", android.widget.Toast.LENGTH_SHORT).show()
        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Fragment_detail_generic.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Fragment_detail_generic().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}