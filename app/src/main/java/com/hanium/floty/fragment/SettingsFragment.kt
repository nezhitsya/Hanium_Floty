package com.hanium.floty.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import com.google.firebase.database.*

import com.hanium.floty.R
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {

    lateinit var mReference: DatabaseReference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.fragment_settings, container, false)

        var first: CardView = view.findViewById(R.id.first)
        var second: CardView = view.findViewById(R.id.second)
        var third: CardView = view.findViewById(R.id.third)
        var fourth: CardView = view.findViewById(R.id.fourth)

        first.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_container, WaterSettingFragment()).addToBackStack(null).commit()
        }

        second.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_container, LightSettingFragment()).addToBackStack(null).commit()
        }

        return view
    }

    fun getWater() {
        mReference = FirebaseDatabase.getInstance().getReference("Water")

        mReference.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

}
