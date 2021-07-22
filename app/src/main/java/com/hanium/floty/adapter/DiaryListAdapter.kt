package com.hanium.floty.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hanium.floty.R
import com.hanium.floty.model.Diary

class DiaryListAdapter(val context: Context, val diaryList: ArrayList<Diary>): RecyclerView.Adapter<DiaryListAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.diary_item, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return diaryList.size
    }

    inner class Holder(itemView: View?): RecyclerView.ViewHolder(itemView!!) {
        val title = itemView?.findViewById<TextView>(R.id.diary_title)
        val weather = itemView?.findViewById<ImageView>(R.id.weather)
        val date = itemView?.findViewById<TextView>(R.id.diary_date)
        val image = itemView?.findViewById<ImageView>(R.id.diary_image)

        fun bind(mDiary: Diary, context: Context) {
            title?.text = mDiary.title
            date?.text = mDiary.date
            image?.let {
                Glide.with(context).load(mDiary.image).into(it)
            }
            if (mDiary.weather == "sunny") {
                weather?.setImageResource(R.drawable.ic_sunny)
            } else if (mDiary.weather == "cloudy") {
                weather?.setImageResource(R.drawable.ic_cloudy)
            } else if (mDiary.weather == "snowy") {
                weather?.setImageResource(R.drawable.ic_snowy)
            } else {
                weather?.setImageResource(R.drawable.ic_rainy)
            }
        }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bind(diaryList[position], context)
    }
}