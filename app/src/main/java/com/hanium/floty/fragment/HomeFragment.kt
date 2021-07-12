package com.hanium.floty.fragment

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

import com.hanium.floty.R
import com.hanium.floty.model.User
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    lateinit var mReference: DatabaseReference
    lateinit var firebaseUser: FirebaseUser

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.fragment_home, container, false)

        profileInfo()

        return view
    }

    private fun profileInfo() {
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        mReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.uid)

        val postListener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user: User? = snapshot.getValue(User::class.java)

                user?.let {
                    nickname.text = user.nickname
                    dday.text = user.day
                    Glide.with(context!!).load(user.profile).into(profile)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }
        mReference.addValueEventListener(postListener)
    }

}
