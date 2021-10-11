package com.hanium.floty.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

import com.hanium.floty.R
import com.hanium.floty.model.Illuminance
import com.hanium.floty.model.Mode
import com.hanium.floty.model.SoilTemp
import com.hanium.floty.model.TempHum
import kotlinx.android.synthetic.main.fragment_light_setting.*
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {

    lateinit var firebaseUser: FirebaseUser
    lateinit var mReference: DatabaseReference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.fragment_settings, container, false)

        var onoff: Button = view.findViewById(R.id.auto_onoff)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        getWater()
        getLight()
        getTemp()
        getMode()

        onoff.setOnClickListener {
            updateOnoff()
        }

        return view
    }

    fun updateOnoff() {
        mReference = FirebaseDatabase.getInstance().getReference("mode").child(firebaseUser.uid)

        val hashMap: HashMap<String, Any> = HashMap()
        if (auto_onoff.text == "ON") {
            hashMap["auto_mode"] = false
            hashMap["manual_mode"] = true
            auto_onoff.text = "OFF"
        } else {
            hashMap["auto_mode"] = true
            hashMap["manual_mode"] = false
            auto_onoff.text = "ON"
        }
        mReference.updateChildren(hashMap)
    }

    fun getWater() {
        var wReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("soil_temp").child(firebaseUser.uid)

        wReference.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                var setting: SoilTemp = snapshot.getValue(SoilTemp::class.java)!!

                setting.let {
                    water_percent.text = setting.soil_rate.toString() + " %"
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun getLight() {
        var lReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("illuminance").child(firebaseUser.uid)

        lReference.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                var setting: Illuminance? = snapshot.getValue(Illuminance::class.java)

                setting?.let {
                    light_percent.text = setting.cds_rate.toString() + " %"
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun getTemp() {
        var tReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("temp_hum").child(firebaseUser.uid)

        tReference.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                var setting: TempHum? = snapshot.getValue(TempHum::class.java)

                setting?.let {
                    temp_percent.text = setting.temp_rate.toString() + " ℃"
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun getMode() {
        var mReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("mode").child(firebaseUser.uid)

        mReference.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                var setting: Mode? = snapshot.getValue(Mode::class.java)

                setting?.let {
                    if (setting.auto_mode) {
                        auto_onoff.text = "ON"
                    } else {
                        auto_onoff.text = "OFF"
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

}
