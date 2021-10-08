package com.hanium.floty.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide

import com.hanium.floty.R
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_profile.*

class DictionaryDetailFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.fragment_dictionary_detail, container, false)

        val preferences: SharedPreferences = context!!.getSharedPreferences("PREFS", Context.MODE_PRIVATE)

        var name: TextView = view.findViewById(R.id.plant_name)
        var engname: TextView = view.findViewById(R.id.plant_engname)
        var image: CircleImageView = view.findViewById(R.id.plant_image)
        var temp: TextView = view.findViewById(R.id.temp_data)
        var hum: TextView = view.findViewById(R.id.hum_data)
        var detail: TextView = view.findViewById(R.id.plant_detail)
        var water: TextView = view.findViewById(R.id.water_data)
        var waterDetail: TextView = view.findViewById(R.id.water_detail_data)

        name.text = preferences.getString("kor", "none").toString()
        engname.text = preferences.getString("eng", "none").toString()
        Glide.with(context!!).load(preferences.getString("imgurl", "none").toString()).into(image)
        temp.text = preferences.getString("temp", "none").toString()
        hum.text = preferences.getString("hum", "none").toString()
        detail.text = preferences.getString("detail", "none").toString()
        water.text = preferences.getString("water", "none").toString()
        waterDetail.text = preferences.getString("waterDetail", "none").toString()

        return view
    }

}
