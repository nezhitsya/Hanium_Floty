package com.hanium.floty.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

import com.hanium.floty.R
import com.hanium.floty.model.Settings
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {

    lateinit var firebaseUser: FirebaseUser

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.fragment_settings, container, false)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        getWater()
        getLight()
        getTemp()
        getFan()

        return view
    }

    fun getWater() {
        var wReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Water").child(firebaseUser.uid)

        wReference.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                var setting: Settings = snapshot.getValue(Settings::class.java)!!

                setting.let {
                    Log.d("day", setting.rate.toString())
                    water_percent.text = setting.rate.toString() + " %"
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun getLight() {
        var lReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Light").child(firebaseUser.uid)

        lReference.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                var setting: Settings? = snapshot.getValue(Settings::class.java)

                setting?.let {
                    light_percent.text = setting.rate.toString() + " %"
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun getTemp() {
        var tReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Temperature").child(firebaseUser.uid)

        tReference.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                var setting: Settings? = snapshot.getValue(Settings::class.java)

                setting?.let {
                    temp_percent.text = setting.rate.toString() + " ℃"
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun getFan() {
        var fReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Fan").child(firebaseUser.uid)

        fReference.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                var setting: Settings? = snapshot.getValue(Settings::class.java)

                setting?.let {
                    fan_percent.text = setting.rate.toString() + " %"
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

}
