package com.hanium.floty.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

import com.hanium.floty.R
import com.hanium.floty.model.Settings
import kotlinx.android.synthetic.main.fragment_history.*

class HistoryFragment : Fragment() {

    lateinit var firebaseUser: FirebaseUser

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.fragment_history, container, false)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        loadWater()
        loadLight()
        loadTemp()
        loadFan()

        return view
    }

    fun loadWater() {
        var wReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("History").child(firebaseUser.uid).child("water")

        val waterListener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var entries = ArrayList<Entry>()

                for (datasnapshot: DataSnapshot in snapshot.children) {
                    for (i in 0..20) {
                        if (datasnapshot.key == i.toString()) {
                            var floatkey = Integer.parseInt(datasnapshot.key.toString())
                            var floatval = Integer.parseInt(datasnapshot.value.toString())
                            entries.add(Entry(floatkey.toFloat(), floatval.toFloat()))
                        }
                    }
                }
                waterGraph(entries)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }
        wReference.addValueEventListener(waterListener)
    }

    fun waterGraph(waterEntry: ArrayList<Entry>) {
        water_chart.setTouchEnabled(false)
//        water_chart.xAxis.labelCount= 30
        water_chart.xAxis.setDrawLabels(false)
        water_chart.xAxis.setDrawGridLines(false)
        water_chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        water_chart.xAxis.axisLineColor = resources.getColor(R.color.colorGray)
        water_chart.axisLeft.axisMaximum = 100f
        water_chart.axisLeft.axisMinimum = 0f
        water_chart.axisLeft.axisLineColor = resources.getColor(R.color.colorGray)
        water_chart.axisLeft.setDrawLabels(false)
        water_chart.axisLeft.setDrawGridLines(false)
        water_chart.axisLeft.setDrawAxisLine(false)
        water_chart.axisRight.isEnabled = false
        water_chart.description = null
        water_chart.legend.isEnabled = false

        var waterDataSet = LineDataSet(waterEntry, "Water")
        waterDataSet.lineWidth = 1f
        waterDataSet.circleRadius = 4f
        waterDataSet.setCircleColor(resources.getColor(R.color.colorBlue))
        waterDataSet.circleHoleColor = resources.getColor(R.color.colorBlue)
        waterDataSet.color = resources.getColor(R.color.colorBlue)
        waterDataSet.setDrawCircleHole(true)
        waterDataSet.setDrawCircles(true)
        waterDataSet.setDrawHorizontalHighlightIndicator(false)
        waterDataSet.setDrawHighlightIndicators(false)
        waterDataSet.setDrawValues(false)

        var waterLineData = LineData(waterDataSet)
        water_chart.data = waterLineData
        water_chart.invalidate()
    }

    fun loadLight() {
        var lReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("History").child(firebaseUser.uid).child("light")

        val lightListener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var entries = ArrayList<Entry>()

                for (datasnapshot: DataSnapshot in snapshot.children) {
                    for (i in 0..20) {
                        if (datasnapshot.key == i.toString()) {
                            var floatkey = Integer.parseInt(datasnapshot.key.toString())
                            var floatval = Integer.parseInt(datasnapshot.value.toString())
                            entries.add(Entry(floatkey.toFloat(), floatval.toFloat()))
                        }
                    }
                }
                lightGraph(entries)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }
        lReference.addValueEventListener(lightListener)
    }

    fun lightGraph(lightEntry: ArrayList<Entry>) {
        light_chart.setTouchEnabled(false)
        light_chart.xAxis.setDrawLabels(false)
        light_chart.xAxis.setDrawGridLines(false)
        light_chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        light_chart.xAxis.axisLineColor = resources.getColor(R.color.colorGray)
        light_chart.axisLeft.axisMaximum = 100f
        light_chart.axisLeft.axisMinimum = 0f
        light_chart.axisLeft.axisLineColor = resources.getColor(R.color.colorGray)
        light_chart.axisLeft.setDrawLabels(false)
        light_chart.axisLeft.setDrawGridLines(false)
        light_chart.axisLeft.setDrawAxisLine(false)
        light_chart.axisRight.isEnabled = false
        light_chart.description = null
        light_chart.legend.isEnabled = false

        var lightDataSet = LineDataSet(lightEntry, "Water")
        lightDataSet.lineWidth = 1f
        lightDataSet.circleRadius = 4f
        lightDataSet.setCircleColor(resources.getColor(R.color.colorYellow))
        lightDataSet.circleHoleColor = resources.getColor(R.color.colorYellow)
        lightDataSet.color = resources.getColor(R.color.colorYellow)
        lightDataSet.setDrawCircleHole(true)
        lightDataSet.setDrawCircles(true)
        lightDataSet.setDrawHorizontalHighlightIndicator(false)
        lightDataSet.setDrawHighlightIndicators(false)
        lightDataSet.setDrawValues(false)

        var lightLineData = LineData(lightDataSet)
        light_chart.data = lightLineData
        light_chart.invalidate()
    }

    fun loadTemp() {
        var tReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("History").child(firebaseUser.uid).child("temp")

        val tempListener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var entries = ArrayList<Entry>()

                for (datasnapshot: DataSnapshot in snapshot.children) {
                    for (i in 0..20) {
                        if (datasnapshot.key == i.toString()) {
                            var floatkey = Integer.parseInt(datasnapshot.key.toString())
                            var floatval = Integer.parseInt(datasnapshot.value.toString())
                            entries.add(Entry(floatkey.toFloat(), floatval.toFloat()))
                        }
                    }
                }
                tempGraph(entries)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }
        tReference.addValueEventListener(tempListener)
    }

    fun tempGraph(tempEntry: ArrayList<Entry>) {
        temp_chart.setTouchEnabled(false)
        temp_chart.xAxis.setDrawLabels(false)
        temp_chart.xAxis.setDrawGridLines(false)
        temp_chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        temp_chart.xAxis.axisLineColor = resources.getColor(R.color.colorGray)
        temp_chart.axisLeft.axisMaximum = 40f
        temp_chart.axisLeft.axisMinimum = -20f
        temp_chart.axisLeft.axisLineColor = resources.getColor(R.color.colorGray)
        temp_chart.axisLeft.setDrawLabels(false)
        temp_chart.axisLeft.setDrawGridLines(false)
        temp_chart.axisLeft.setDrawAxisLine(false)
        temp_chart.axisRight.isEnabled = false
        temp_chart.description = null
        temp_chart.legend.isEnabled = false

        var tempDataSet = LineDataSet(tempEntry, "Temp")
        tempDataSet.lineWidth = 1f
        tempDataSet.circleRadius = 4f
        tempDataSet.setCircleColor(resources.getColor(R.color.colorRed))
        tempDataSet.circleHoleColor = resources.getColor(R.color.colorRed)
        tempDataSet.color = resources.getColor(R.color.colorRed)
        tempDataSet.setDrawCircleHole(true)
        tempDataSet.setDrawCircles(true)
        tempDataSet.setDrawHorizontalHighlightIndicator(false)
        tempDataSet.setDrawHighlightIndicators(false)
        tempDataSet.setDrawValues(false)

        var tempLineData = LineData(tempDataSet)
        temp_chart.data = tempLineData
        temp_chart.invalidate()
    }

    fun loadFan() {
        var tReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("History").child(firebaseUser.uid).child("fan")

        val tempListener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var entries = ArrayList<Entry>()

                for (datasnapshot: DataSnapshot in snapshot.children) {
                    for (i in 0..20) {
                        if (datasnapshot.key == i.toString()) {
                            var floatkey = Integer.parseInt(datasnapshot.key.toString())
                            var floatval = Integer.parseInt(datasnapshot.value.toString())
                            entries.add(Entry(floatkey.toFloat(), floatval.toFloat()))
                        }
                    }
                }
                fanGraph(entries)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }
        tReference.addValueEventListener(tempListener)
    }

    fun fanGraph(fanEntry: ArrayList<Entry>) {
        fan_chart.setTouchEnabled(false)
        fan_chart.xAxis.setDrawLabels(false)
        fan_chart.xAxis.setDrawGridLines(false)
        fan_chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        fan_chart.xAxis.axisLineColor = resources.getColor(R.color.colorGray)
        fan_chart.axisLeft.axisMaximum = 100f
        fan_chart.axisLeft.axisMinimum = 0f
        fan_chart.axisLeft.axisLineColor = resources.getColor(R.color.colorGray)
        fan_chart.axisLeft.setDrawLabels(false)
        fan_chart.axisLeft.setDrawGridLines(false)
        fan_chart.axisLeft.setDrawAxisLine(false)
        fan_chart.axisRight.isEnabled = false
        fan_chart.description = null
        fan_chart.legend.isEnabled = false

        var fanDataSet = LineDataSet(fanEntry, "Temp")
        fanDataSet.lineWidth = 1f
        fanDataSet.circleRadius = 4f
        fanDataSet.setCircleColor(resources.getColor(R.color.colorDarkBlue))
        fanDataSet.circleHoleColor = resources.getColor(R.color.colorDarkBlue)
        fanDataSet.color = resources.getColor(R.color.colorDarkBlue)
        fanDataSet.setDrawCircleHole(true)
        fanDataSet.setDrawCircles(true)
        fanDataSet.setDrawHorizontalHighlightIndicator(false)
        fanDataSet.setDrawHighlightIndicators(false)
        fanDataSet.setDrawValues(false)

        var fanLineData = LineData(fanDataSet)
        fan_chart.data = fanLineData
        fan_chart.invalidate()
    }

}
