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
 * Use the [FragmentProfil.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentProfil : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_profil, container, false)
        
        // Initialize SessionManager
        val sessionManager = SessionManager(requireContext())
        
        // Find Views
        val tvName = view.findViewById<android.widget.TextView>(R.id.tv_name)
        val etUsername = view.findViewById<android.widget.EditText>(R.id.et_username)
        val etPhone = view.findViewById<android.widget.EditText>(R.id.et_phone)
        val etEmail = view.findViewById<android.widget.EditText>(R.id.et_email)
        
        // Header Back Button
        val btnBack = view.findViewById<android.view.View>(R.id.btn_back)
        btnBack.setOnClickListener {
            // Optional: Go back or Home
             startActivity(android.content.Intent(context, ActivityHome::class.java))
        }

        // Populate Data
        val username = sessionManager.getUsername()
        val email = sessionManager.getEmail()
        val phone = sessionManager.getPhone()
        
        tvName.text = username ?: "Raditya"
        etUsername.setText(username)
        etEmail.setText(email)
        etPhone.setText(phone ?: "-")
        
        // BOTTOM NAVIGATION LOGIC
        // 1. Home
        val navHome = view.findViewById<android.widget.ImageView>(R.id.nav_home)
        navHome.setOnClickListener {
            startActivity(android.content.Intent(context, ActivityHome::class.java))
        }

        // 2. Ticket (Paket Wisata -> TicketActivity)
        val navTicket = view.findViewById<android.widget.ImageView>(R.id.nav_paket)
        navTicket.setOnClickListener {
             startActivity(android.content.Intent(context, TicketActivity::class.java))
        }

        // 3. Wishlist (Love)
        val navFav = view.findViewById<android.widget.ImageView>(R.id.nav_fav)
        navFav.setOnClickListener {
            startActivity(android.content.Intent(context, WishlistActivity::class.java))
        }

        // 4. Profile (Settings/Person) - Already Here
        val navProfile = view.findViewById<android.view.View>(R.id.nav_profile_active)
        navProfile.setOnClickListener {
            // Already here
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
         * @return A new instance of fragment FragmentProfil.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentProfil().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}