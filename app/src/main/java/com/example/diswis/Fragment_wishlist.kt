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
 * Use the [Fragment_wishlist.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment_wishlist : Fragment() {
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
        return inflater.inflate(R.layout.fragment_wishlist, container, false)
    }

    private lateinit var adapter: DestinationAdapter
    private lateinit var emptyState: View
    private lateinit var rvWishlist: androidx.recyclerview.widget.RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvWishlist = view.findViewById(R.id.rvWishlist)
        emptyState = view.findViewById(R.id.emptyState)
        val btnBack = view.findViewById<android.widget.ImageView>(R.id.btnBack)

        rvWishlist.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        adapter = DestinationAdapter(ArrayList())
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Fragment_wishlist.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Fragment_wishlist().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}