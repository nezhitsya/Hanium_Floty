package com.hanium.floty.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
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
    var soilEntry = ArrayList<PieEntry>()
    var cdsEntry = ArrayList<PieEntry>()
    var humEntry = ArrayList<PieEntry>()
    var tempEntry = ArrayList<PieEntry>()

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
                soilEntry.clear()
                var setting: SoilTemp = snapshot.getValue(SoilTemp::class.java)!!

                setting.let {
                    var percentage = Math.round((setting.soil_rate * 100 / 1023).toDouble())
                    soilEntry.add(PieEntry(percentage.toFloat(), ""))
                    soilEntry.add(PieEntry((100 - percentage).toFloat(), ""))
                    water_percent.text = "$percentage %"
                }
                waterGraph(soilEntry)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun waterGraph(waterEntry: ArrayList<PieEntry>) {
        soil_chart.setUsePercentValues(true)

        var colorsItem = ArrayList<Int>()
        colorsItem.add(resources.getColor(R.color.colorBlue))
        colorsItem.add(resources.getColor(R.color.colorLightGray))

        var pieDataSet = PieDataSet(waterEntry, "")
        pieDataSet.apply {
            colors = colorsItem
            setDrawValues(false)
            sliceSpace = 3f
        }

        var pieData = PieData(pieDataSet)
        soil_chart.apply {
            data = pieData
            description = null
            centerText = "토양 습도"
            legend.isEnabled = false
            animateY(1400, Easing.EaseInOutQuad)
            animate()
            invalidate()
        }
    }

    fun getLight() {
        var lReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("illuminance").child(firebaseUser.uid)

        lReference.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                cdsEntry.clear()
                var setting: Illuminance? = snapshot.getValue(Illuminance::class.java)

                setting?.let {
                    var percentage = Math.round((setting.cds_rate * 100 / 1023).toDouble())
                    cdsEntry.add(PieEntry(percentage.toFloat(), ""))
                    cdsEntry.add(PieEntry((100 - percentage).toFloat(), ""))
                    light_percent.text = "$percentage %"
                }
                cdsGraph(cdsEntry)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun cdsGraph(lightEntry: ArrayList<PieEntry>) {
        cds_chart.setUsePercentValues(true)

        var colorsItem = ArrayList<Int>()
        colorsItem.add(resources.getColor(R.color.colorYellow))
        colorsItem.add(resources.getColor(R.color.colorLightGray))

        var pieDataSet = PieDataSet(lightEntry, "")
        pieDataSet.apply {
            colors = colorsItem
            setDrawValues(false)
            sliceSpace = 3f
        }

        var pieData = PieData(pieDataSet)
        cds_chart.apply {
            data = pieData
            description = null
            centerText = "조도"
            legend.isEnabled = false
            animateY(1400, Easing.EaseInOutQuad)
            animate()
            invalidate()
        }
    }

    fun getTemp() {
        var tReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("temp_hum").child(firebaseUser.uid)

        tReference.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                humEntry.clear()
                tempEntry.clear()
                var setting: TempHum? = snapshot.getValue(TempHum::class.java)

                setting?.let {
                    var percentage = Math.round((setting.hum_rate * 100 / 1023).toDouble())
                    temp_percent.text = setting.temp_rate.toString() + " ℃"
                    hum_percent.text = "$percentage %"
                    humEntry.add(PieEntry(percentage.toFloat(), ""))
                    humEntry.add(PieEntry((100 - percentage).toFloat(), ""))

                    tempEntry.add(PieEntry(setting.temp_rate.toFloat(), ""))
                    tempEntry.add(PieEntry(50 - setting.temp_rate.toFloat(), ""))
                }
                humGraph(humEntry)
                tempGraph(tempEntry)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun humGraph(humEntry: ArrayList<PieEntry>) {
        hum_chart.setUsePercentValues(true)

        var colorsItem = ArrayList<Int>()
        colorsItem.add(resources.getColor(R.color.colorDarkBlue))
        colorsItem.add(resources.getColor(R.color.colorLightGray))

        var pieDataSet = PieDataSet(humEntry, "")
        pieDataSet.apply {
            colors = colorsItem
            setDrawValues(false)
            sliceSpace = 3f
        }

        var pieData = PieData(pieDataSet)
        hum_chart.apply {
            data = pieData
            description = null
            centerText = "습도"
            legend.isEnabled = false
            animateY(1400, Easing.EaseInOutQuad)
            animate()
            invalidate()
        }
    }

    fun tempGraph(tempEntry: ArrayList<PieEntry>) {
        var colorsItem = ArrayList<Int>()
        colorsItem.add(resources.getColor(R.color.colorRed))
        colorsItem.add(resources.getColor(R.color.colorLightGray))

        var pieDataSet = PieDataSet(tempEntry, "")
        pieDataSet.apply {
            colors = colorsItem
            setDrawValues(false)
            sliceSpace = 3f
        }

        var pieData = PieData(pieDataSet)
        temp_chart.apply {
            data = pieData
            description = null
            centerText = "온도"
            legend.isEnabled = false
            animateY(1400, Easing.EaseInOutQuad)
            animate()
            invalidate()
        }
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
