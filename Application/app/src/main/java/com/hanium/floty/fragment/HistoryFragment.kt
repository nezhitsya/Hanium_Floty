package com.hanium.floty.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

import com.hanium.floty.R
import com.hanium.floty.model.History
import kotlinx.android.synthetic.main.fragment_history.*

class HistoryFragment : Fragment() {

    lateinit var firebaseUser: FirebaseUser
    var soilEntry = ArrayList<Entry>()
    var cdsEntry = ArrayList<Entry>()
    var humEntry = ArrayList<Entry>()
    var tempEntry = ArrayList<Entry>()
    val time: Array<Int> = arrayOf(1, 2, 3, 4, 5)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.fragment_history, container, false)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        loadWater() // soilEntry
        loadLight() // cdsEntry
        loadTemp() // tempEntry
        loadFan() // humEntry

        return view
    }

    fun loadWater() {
        var reference: DatabaseReference = FirebaseDatabase.getInstance().getReference("history").child(firebaseUser.uid)

        for (t in time) {
            var wReference = reference.child(t.toString())
            wReference.addValueEventListener(object: ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    val history: History? = snapshot.getValue(History::class.java)
                    var floatval = Integer.parseInt(history?.soil_rate.toString())

                    soilEntry.add(Entry(t.toFloat(), floatval.toFloat()))
                    waterGraph(soilEntry)
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }
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
        var reference: DatabaseReference = FirebaseDatabase.getInstance().getReference("history").child(firebaseUser.uid)

        for (t in time) {
            var lReference = reference.child(t.toString())

            val lightListener = object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val history: History? = snapshot.getValue(History::class.java)
                    var floatval = Integer.parseInt(history?.cds_rate.toString())

                    cdsEntry.add(Entry(t.toFloat(), floatval.toFloat()))
                    lightGraph(cdsEntry)
                }

                override fun onCancelled(error: DatabaseError) {

                }
            }

            lReference.addValueEventListener(lightListener)
        }

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
        var reference: DatabaseReference = FirebaseDatabase.getInstance().getReference("history").child(firebaseUser.uid)

        for (t in time) {
            var tReference = reference.child(t.toString())

            val tempListener = object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val history: History? = snapshot.getValue(History::class.java)
                    var floatval = Integer.parseInt(history?.temp_rate.toString())

                    tempEntry.add(Entry(t.toFloat(), floatval.toFloat()))
                    tempGraph(tempEntry)
                }

                override fun onCancelled(error: DatabaseError) {

                }
            }

            tReference.addValueEventListener(tempListener)
        }

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
        var reference: DatabaseReference = FirebaseDatabase.getInstance().getReference("history").child(firebaseUser.uid)

        for (t in time) {
            var hReference = reference.child(t.toString())

            val humListener = object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val history: History? = snapshot.getValue(History::class.java)
                    var floatval = Integer.parseInt(history?.hum_rate.toString())

                    humEntry.add(Entry(t.toFloat(), floatval.toFloat()))
                    fanGraph(humEntry)
                }

                override fun onCancelled(error: DatabaseError) {

                }
            }

            hReference.addValueEventListener(humListener)
        }
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
