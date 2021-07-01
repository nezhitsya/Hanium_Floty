package com.hanium.floty.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hanium.floty.R
import com.hanium.floty.model.Item

class DictionaryAdapter(val context: Context, val plantList: ArrayList<Item>): RecyclerView.Adapter<DictionaryAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.dictionary_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(plantList[position], context)
    }

    override fun getItemCount(): Int {
        return plantList.size
    }

    inner class Holder(itemView: View?): RecyclerView.ViewHolder(itemView!!) {
        var image = itemView?.findViewById<ImageView>(R.id.plant_image)
        var name = itemView?.findViewById<TextView>(R.id.plant_name)
        var info = itemView?.findViewById<TextView>(R.id.plant_info)

        fun bind(mPlant: Item, context: Context){
            val urlString1 = mPlant?.rtnFileCours.toString()
            val urlString2 = mPlant?.rtnStreFileNm.toString()

            if(!urlString1.isEmpty() or !urlString2.isEmpty()){
                image?.setImageResource(R.mipmap.ic_launcher)
            } else{
                image?.visibility = View.GONE
            }
            name?.text = mPlant?.cntntsSj
        }
    }

}
