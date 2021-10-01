package com.hanium.floty.adapter

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hanium.floty.MainActivity
import com.hanium.floty.R
import com.hanium.floty.fragment.DiaryDetailFragment
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
        val imageContainer = itemView?.findViewById<CardView>(R.id.image)

        var bundle = Bundle()

        fun bind(mDiary: Diary, context: Context) {
            title?.text = mDiary.title
            date?.text = mDiary.year + " 년 " + mDiary.month + " 월 " + mDiary.day + " 일"

            if (mDiary.image == "null") {
                imageContainer!!.visibility = View.GONE
                image!!.visibility = View.GONE
            } else {
                imageContainer!!.visibility = View.VISIBLE
                image!!.visibility = View.VISIBLE
                image?.let {
                    Glide.with(context).load(mDiary.image).into(it)
                }
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

            itemView.setOnClickListener {
                var editor: SharedPreferences.Editor = context.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
                editor.putString("diaryid", mDiary.diaryid)
                editor.apply()

                var year1: Int = Integer.parseInt(mDiary.year!!)
                var month1: Int = Integer.parseInt(mDiary.month!!)
                var day1: Int = Integer.parseInt(mDiary.day!!)

                val fragment = (context as MainActivity).supportFragmentManager.beginTransaction()
                fragment.replace(R.id.fragment_container, DiaryDetailFragment().apply {
                    arguments = bundle.apply {
                        putInt("year", year1)
                        putInt("month", month1)
                        putInt("day", day1)
                    }
                }).addToBackStack(null).commit()
            }
        }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bind(diaryList[position], context)
    }
}