package com.hanium.floty.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hanium.floty.PicSearchActivity
import com.hanium.floty.R
import com.hanium.floty.adapter.DictionaryAdapter
import com.hanium.floty.model.Plant
import de.hdodenhof.circleimageview.CircleImageView
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException

class DictionaryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    var plantList = arrayListOf<Plant>()
    var searchList = arrayListOf<Plant>()
    var beforeSearchList = arrayListOf<Plant>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        var view: View = inflater.inflate(R.layout.fragment_dictionary, container, false)

        var search: EditText = view.findViewById(R.id.search_bar)
        var imageSearch: ImageView = view.findViewById(R.id.search_image)
        var first: CircleImageView = view.findViewById(R.id.first)
        var second: CircleImageView = view.findViewById(R.id.second)
        var third: CircleImageView = view.findViewById(R.id.third)
        var fourth: CircleImageView = view.findViewById(R.id.fourth)

        search.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchPlant(s.toString().toLowerCase())
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        imageSearch.setOnClickListener {
            val intent = Intent(getActivity(), PicSearchActivity::class.java)
            startActivity(intent)
        }

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
                        beforeSearchList.add(plant)
                    }
                }
                xpp.next()
            }
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        first.let {
            Glide.with(this).load(plantList[1].imgurl).into(it)
        }

        second.let {
            Glide.with(this).load(plantList[2].imgurl).into(it)
        }

        third.let {
            Glide.with(this).load(plantList[3].imgurl).into(it)
        }

        fourth.let {
            Glide.with(this).load(plantList[4].imgurl).into(it)
        }

        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)
        var linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        recyclerView.layoutManager = linearLayoutManager

        val adapter = DictionaryAdapter(context!!, beforeSearchList)
        recyclerView?.adapter = adapter
        adapter.notifyDataSetChanged()

        return view
    }

    private fun searchPlant(s: String) {
        if (s == "") {
            beforeSearchList.clear()

            val adapter = DictionaryAdapter(context!!, plantList)
            recyclerView?.adapter = adapter
            adapter.notifyDataSetChanged()
        } else {
            searchList.clear()
            for (i in 0 until plantList.size) {
                if (plantList[i].plantkor.contains(s)) {
                    searchList.add(plantList[i])
                }
            }

            val adapter = DictionaryAdapter(context!!, searchList)
            recyclerView?.adapter = adapter
            adapter.notifyDataSetChanged()
        }
    }

}