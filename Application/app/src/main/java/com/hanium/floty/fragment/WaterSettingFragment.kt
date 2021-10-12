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
import com.hanium.floty.model.SoilTemp
import kotlinx.android.synthetic.main.fragment_water_setting.*

class WaterSettingFragment : Fragment() {

    lateinit var firebaseUser: FirebaseUser
    lateinit var mReference: DatabaseReference
//    lateinit var connectedThread: ConnectedThread

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.fragment_water_setting, container, false)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        var seekbar_water: SeekBar = view.findViewById(R.id.range)
        var seekBar_time: SeekBar = view.findViewById(R.id.time_range)
        var percent: TextView = view.findViewById(R.id.percent)
        var cloudImg: ImageView = view.findViewById(R.id.cloud)
        var onoffTxt: TextView = view.findViewById(R.id.onoff)

//        if (connectedThread != null) {
//            connectedThread.write("hi!!!")
//        }

        seekbar_water.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                var percentage = Math.round((progress * 100 / 1023).toDouble())
                percent.text = "$percentage %"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                var percentage = Math.round((seekBar!!.progress * 100 / 1023).toDouble())
                percent.text = "${percentage} %"
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                var percentage = Math.round((seekBar!!.progress * 100 / 1023).toDouble())
                percent.text = "${percentage} %"
                updateRate(seekBar!!.progress)
            }
        })

        seekBar_time.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                var percentage = progress / 1000
                percent.text = "$percentage 초"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                var percentage = seekBar!!.progress / 1000
                percent.text = "${percentage} 초"
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                var percentage = seekBar!!.progress / 1000
                percent.text = "${percentage} 초"
                updateTime(seekBar!!.progress)
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
        mReference = FirebaseDatabase.getInstance().getReference("soil_temp").child(firebaseUser.uid)

        val waterListener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var setting: SoilTemp? = snapshot.getValue(SoilTemp::class.java)

                setting?.let {
                    range.setProgress(setting.soil_auto_set)
                    time_range.setProgress(setting.pump_uptime)

                    var percentage = Math.round((setting.soil_auto_set * 100 / 1023).toDouble())
                    percent.text = percentage.toString() + " %"

                    if (setting.pump_onoff.equals("on")) {
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
        mReference = FirebaseDatabase.getInstance().getReference("soil_temp").child(firebaseUser.uid)
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["soil_auto_set"] = rateVal
        mReference.updateChildren(hashMap)
    }

    fun updateTime(rateVal: Int) {
        mReference = FirebaseDatabase.getInstance().getReference("soil_temp").child(firebaseUser.uid)
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["pump_uptime"] = rateVal
        mReference.updateChildren(hashMap)
    }

    fun updateOnoff() {
        mReference = FirebaseDatabase.getInstance().getReference("soil_temp").child(firebaseUser.uid)

        val hashMap: HashMap<String, Any> = HashMap()
        if (onoff.text == "ON") {
            hashMap["pump_onoff"] = false
            onoff.text = "OFF"
        } else {
            hashMap["pump_onoff"] = true
            onoff.text = "ON"
        }
        mReference.updateChildren(hashMap)
    }

}
