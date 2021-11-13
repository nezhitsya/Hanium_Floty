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
import com.hanium.floty.model.TempHum
import kotlinx.android.synthetic.main.fragment_temperature_setting.*

class TemperatureSettingFragment : Fragment() {

    lateinit var firebaseUser: FirebaseUser
    lateinit var mReference: DatabaseReference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view: View =  inflater.inflate(R.layout.fragment_temperature_setting, container, false)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        var seekbar_temp: SeekBar = view.findViewById(R.id.range)
        var seekbar_time: SeekBar = view.findViewById(R.id.time_range)
        var percent: TextView = view.findViewById(R.id.percent)
        var tempImg: ImageView = view.findViewById(R.id.cloud)
        var onoffTxt: TextView = view.findViewById(R.id.onoff)

        seekbar_temp.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                percent.text = "$progress ℃"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                percent.text = "${seekBar!!.progress} ℃"
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                percent.text = "${seekBar!!.progress} ℃"
                updateRate(seekBar!!.progress)
            }
        })

        seekbar_time.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
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

        tempImg.setOnClickListener {
            updateOnoff()
        }

        onoffTxt.setOnClickListener {
            updateOnoff()
        }

        loadRate()

        return view
    }

    fun loadRate() {
        mReference = FirebaseDatabase.getInstance().getReference("temp_hum").child(firebaseUser.uid)

        val tempListener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var setting: TempHum? = snapshot.getValue(TempHum::class.java)

                setting?.let {
                    range.setProgress(setting.temp_auto_set)
                    time_range.setProgress(setting.fan_uptime)
                    percent.text = setting.temp_auto_set.toString() + " ℃"

                    if (setting.fan_onoff.equals("on")) {
                        onoff.text = "ON"
                    } else {
                        onoff.text = "OFF"
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }

        mReference.addValueEventListener(tempListener)
    }

    fun updateRate(rateVal: Int) {
        mReference = FirebaseDatabase.getInstance().getReference("temp_hum").child(firebaseUser.uid)
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["temp_auto_set"] = rateVal
        mReference.updateChildren(hashMap)
    }

    fun updateTime(rateVal: Int) {
        mReference = FirebaseDatabase.getInstance().getReference("temp_hum").child(firebaseUser.uid)
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["fan_uptime"] = rateVal
        mReference.updateChildren(hashMap)
    }

    fun updateOnoff() {
        mReference = FirebaseDatabase.getInstance().getReference("temp_hum").child(firebaseUser.uid)

        val hashMap: HashMap<String, Any> = HashMap()
        if (onoff.text == "ON") {
            hashMap["fan_onoff"] = "off"
            onoff.text = "OFF"
        } else {
            hashMap["fan_onoff"] = "on"
            onoff.text = "ON"
        }
        mReference.updateChildren(hashMap)
    }

}
