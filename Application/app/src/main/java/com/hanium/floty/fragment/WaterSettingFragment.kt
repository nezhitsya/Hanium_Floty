package com.hanium.floty.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

import com.hanium.floty.R
import com.hanium.floty.adapter.ConnectedThread
import com.hanium.floty.model.Settings
import kotlinx.android.synthetic.main.fragment_water_setting.*

class WaterSettingFragment : Fragment() {

    lateinit var firebaseUser: FirebaseUser
    lateinit var mReference: DatabaseReference
    lateinit var connectedThread: ConnectedThread

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.fragment_water_setting, container, false)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        var seekbar_water: SeekBar = view.findViewById(R.id.range)
        var percent: TextView = view.findViewById(R.id.percent)
        var cloudImg: ImageView = view.findViewById(R.id.cloud)
        var onoffTxt: TextView = view.findViewById(R.id.onoff)

//        if (connectedThread != null) {
//            connectedThread.write("hi!!!")
//        }

        seekbar_water.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                percent.text = "$progress %"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                percent.text = "${seekBar!!.progress} %"
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                percent.text = "${seekBar!!.progress} %"
                updateRate(seekBar!!.progress)
            }
        })

        cloudImg.setOnClickListener {
            updateOnoff()
        }

        onoffTxt.setOnClickListener {
            updateOnoff()
        }

        loadRate()

        return view
    }

    fun loadRate() {
        mReference = FirebaseDatabase.getInstance().getReference("Water").child(firebaseUser.uid)

        val waterListener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var setting: Settings? = snapshot.getValue(Settings::class.java)

                setting?.let {
                    range.setProgress(setting.rate)
                    percent.text = setting.rate.toString() + " %"

                    if (setting.onoff.equals(true)) {
                        onoff.text = "ON"
                    } else {
                        onoff.text = "OFF"
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }

        mReference.addValueEventListener(waterListener)
    }

    fun updateRate(rateVal: Int) {
        mReference = FirebaseDatabase.getInstance().getReference("Water").child(firebaseUser.uid)
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["rate"] = rateVal
        mReference.updateChildren(hashMap)
    }

    fun updateOnoff() {
        mReference = FirebaseDatabase.getInstance().getReference("Water").child(firebaseUser.uid)

        val hashMap: HashMap<String, Any> = HashMap()
        if (onoff.text == "ON") {
            hashMap["onoff"] = false
            onoff.text = "OFF"
        } else {
            hashMap["onoff"] = true
            onoff.text = "ON"
        }
        mReference.updateChildren(hashMap)
    }

}
