package com.hanium.floty.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

import com.hanium.floty.R

class HomeFragment : Fragment() {

    lateinit var mReference: DatabaseReference
    lateinit var firebaseUser: FirebaseUser

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        var view: View = inflater.inflate(R.layout.fragment_home, container, false)

        var profile: ImageView = view.findViewById(R.id.profile)
        var nickname: TextView = view.findViewById(R.id.nickname)
        var dday: TextView = view.findViewById(R.id.dday)

        profileInfo()

        return view
    }

    private fun profileInfo() {
    }

}
