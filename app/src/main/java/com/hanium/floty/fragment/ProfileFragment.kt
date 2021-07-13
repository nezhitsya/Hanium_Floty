package com.hanium.floty.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.hanium.floty.EditProfileActivity
import com.hanium.floty.QRActivity

import com.hanium.floty.R
import com.hanium.floty.model.User
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    lateinit var firebaseUser: FirebaseUser
    lateinit var mReference: DatabaseReference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.fragment_profile, container, false)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        var logout: TextView = view.findViewById(R.id.logout)
        var resign: TextView = view.findViewById(R.id.resign)
        var setting: ImageView = view.findViewById(R.id.setting)

        logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            startActivity(Intent(context!!, QRActivity::class.java))
        }

        resign.setOnClickListener {
            FirebaseAuth.getInstance().currentUser!!.delete().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    FirebaseAuth.getInstance().signOut()

                    startActivity(Intent(context!!, QRActivity::class.java))
                } else {
                    Toast.makeText(context, "다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        setting.setOnClickListener {
            startActivity(Intent(context!!, EditProfileActivity::class.java))
        }

        userInfo()

        return view
    }

    private fun userInfo() {
        mReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.uid)

        val userListener = object: ValueEventListener {
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
        mReference.addValueEventListener(userListener)
    }

}
