package com.hanium.floty.fragment

import android.content.res.AssetManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hanium.floty.R
import com.hanium.floty.adapter.DictionaryAdapter
import com.hanium.floty.model.Plant
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException

class DictionaryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    var plantList = arrayListOf<Plant>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        var view: View = inflater.inflate(R.layout.fragment_dictionary, container, false)

        try {
            var xpp: XmlPullParser = resources.getXml(R.xml.plant)
            plantList.clear()

            while (xpp.eventType != XmlPullParser.END_DOCUMENT) {
                if (xpp.eventType == XmlPullParser.START_TAG) {
                    if (xpp.name.equals("plant")) {
                        val plant = Plant()
//                        Log.d("plant", xpp.getAttributeValue(0)) // detail
//                        Log.d("plant", xpp.getAttributeValue(1)) // hum
//                        Log.d("plant", xpp.getAttributeValue(2)) // img
//                        Log.d("plant", xpp.getAttributeValue(3)) // eng
//                        Log.d("plant", xpp.getAttributeValue(4)) // kor
//                        Log.d("plant", xpp.getAttributeValue(5)) // temp
//                        Log.d("plant", xpp.getAttributeValue(6)) // water
//                        Log.d("plant", xpp.getAttributeValue(7)) // water detail

                        plant.plantkor = xpp.getAttributeValue(4)
                        plant.planteng = xpp.getAttributeValue(3)
                        plant.temp = xpp.getAttributeValue(5)
                        plant.hum = xpp.getAttributeValue(1)
                        plant.imgurl = xpp.getAttributeValue(2)
                        plant.detail = xpp.getAttributeValue(0)
                        plant.water= xpp.getAttributeValue(6)
                        plant.waterDetail = xpp.getAttributeValue(7)

                        plantList.add(plant)
                    }
                }
                xpp.next()
            }
            Log.e("MY_VALUE", plantList.toString())
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)
        var linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        recyclerView.layoutManager = linearLayoutManager

        val adapter = DictionaryAdapter(context!!, plantList)
        recyclerView?.adapter = adapter
        adapter.notifyDataSetChanged()

        return view
    }

}