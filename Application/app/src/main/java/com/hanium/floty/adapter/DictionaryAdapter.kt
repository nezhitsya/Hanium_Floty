package com.hanium.floty.adapter

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hanium.floty.MainActivity
import com.hanium.floty.R
import com.hanium.floty.fragment.DictionaryDetailFragment
import com.hanium.floty.fragment.PostDetailFragment
import com.hanium.floty.model.Plant
import java.util.*

class DictionaryAdapter(val context: Context, val plantList: ArrayList<Plant>): RecyclerView.Adapter<DictionaryAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DictionaryAdapter.Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.dictionary_item, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return plantList.size
    }

    inner class Holder(itemView: View?): RecyclerView.ViewHolder(itemView!!) {
        var image = itemView?.findViewById<ImageView>(R.id.plant_image)
        var korName = itemView?.findViewById<TextView>(R.id.plant_name)
        var engName = itemView?.findViewById<TextView>(R.id.plant_engname)

        fun bind(mPlant: Plant, context: Context) {
            korName?.text = mPlant.plantkor
            engName?.text = mPlant.planteng
            image?.let {
                Glide.with(context).load(mPlant.imgurl).into(it)
            }

            itemView.setOnClickListener {
                var editor: SharedPreferences.Editor = context.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
                editor.putString("kor", mPlant.plantkor)
                editor.putString("eng", mPlant.planteng)
                editor.putString("temp", mPlant.temp)
                editor.putString("hum", mPlant.hum)
                editor.putString("imgurl", mPlant.imgurl)
                editor.putString("detail", mPlant.detail)
                editor.putString("water", mPlant.water)
                editor.putString("waterDetail", mPlant.waterDetail)
                editor.apply()

                val fragment = (context as MainActivity).supportFragmentManager.beginTransaction()
                fragment.replace(R.id.fragment_container, DictionaryDetailFragment()).addToBackStack(null).commit()
            }
        }
    }

    override fun onBindViewHolder(holder: DictionaryAdapter.Holder, position: Int) {
        holder?.bind(plantList[position], context)
    }
}