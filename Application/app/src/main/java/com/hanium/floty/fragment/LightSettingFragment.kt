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
import com.hanium.floty.model.Illuminance
import kotlinx.android.synthetic.main.fragment_light_setting.*

class LightSettingFragment : Fragment() {

    lateinit var firebaseUser: FirebaseUser
    lateinit var mReference: DatabaseReference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.fragment_light_setting, container, false)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        var seekbar_led: SeekBar = view.findViewById(R.id.range)
        var seekbar_light: SeekBar = view.findViewById(R.id.light_range)
        var percent: TextView = view.findViewById(R.id.percent)
        var lightImg: ImageView = view.findViewById(R.id.cloud)
        var onoffTxt: TextView = view.findViewById(R.id.onoff)

        seekbar_led.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                var percentage = Math.round((progress * 100 / 255).toDouble())
                percent.text = "$percentage %"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                var percentage = Math.round((seekBar!!.progress * 100 / 255).toDouble())
                percent.text = "${percentage} %"
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                var percentage = Math.round((seekBar!!.progress * 100 / 255).toDouble())
                percent.text = "${percentage} %"
                updateRate(seekBar!!.progress)
            }
        })

        seekbar_light.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
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
                updateLight(seekBar!!.progress)
            }
        })

        lightImg.setOnClickListener {
            updateOnoff()
        }

        onoffTxt.setOnClickListener {
            updateOnoff()
        }

        loadRate()

        return view
    }

    fun loadRate() {
        mReference = FirebaseDatabase.getInstance().getReference("illuminance").child(firebaseUser.uid)

        val lightListener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var setting: Illuminance? = snapshot.getValue(Illuminance::class.java)

                setting?.let {
                    range.setProgress(setting.led_brightness)
                    light_range.setProgress(setting.cds_auto_set)

                    var percentage = Math.round((setting.led_brightness * 100 / 255).toDouble())
                    percent.text = percentage.toString() + " %"

                    if (setting.led_onoff.equals("on")) {
                        onoff.text = "ON"
                    } else {
                        onoff.text = "OFF"
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }

        mReference.addValueEventListener(lightListener)
    }

    fun updateRate(rateVal: Int) {
        mReference = FirebaseDatabase.getInstance().getReference("illuminance").child(firebaseUser.uid)
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["led_brightness"] = rateVal
        mReference.updateChildren(hashMap)
    }

    fun updateLight(rateVal: Int) {
        mReference = FirebaseDatabase.getInstance().getReference("illuminance").child(firebaseUser.uid)
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["cds_auto_set"] = rateVal
        mReference.updateChildren(hashMap)
    }

    fun updateOnoff() {
        mReference = FirebaseDatabase.getInstance().getReference("illuminance").child(firebaseUser.uid)

        val hashMap: HashMap<String, Any> = HashMap()
        if (onoff.text == "ON") {
            hashMap["led_onoff"] = "off"
            onoff.text = "OFF"
        } else {
            hashMap["led_onoff"] = "on"
            onoff.text = "ON"
        }
        mReference.updateChildren(hashMap)
    }

}
