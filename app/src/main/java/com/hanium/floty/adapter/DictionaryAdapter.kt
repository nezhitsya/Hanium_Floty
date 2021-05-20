package com.hanium.floty.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hanium.floty.R
import com.hanium.floty.model.PlantInfo

class DictionaryAdapter(val context: Context, val plantList: ArrayList<PlantInfo>): RecyclerView.Adapter<DictionaryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return DictionaryAdapter.ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.dictionary_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(plantList[position], context)
    }

    override fun getItemCount(): Int {
        return plantList.count()
    }

    class ViewHolder(itemView: View?): RecyclerView.ViewHolder(itemView!!) {
        var image = itemView?.findViewById<ImageView>(R.id.plant_image)
        var name = itemView?.findViewById<TextView>(R.id.plant_name)
        var info = itemView?.findViewById<TextView>(R.id.plant_info)

        fun bind(mPlant: PlantInfo, context: Context){
            val urlString = mPlant?.imageEvlLinkCours.toString()

            if(!urlString.isEmpty()){
                image?.setImageResource(R.mipmap.ic_launcher)
            } else{
                image?.visibility = View.GONE
            }
            name?.text = mPlant?.distbNm
            info?.text = mPlant?.hdCode
        }
    }

}
