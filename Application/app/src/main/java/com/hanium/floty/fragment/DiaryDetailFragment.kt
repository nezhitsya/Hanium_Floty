package com.hanium.floty.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

import com.hanium.floty.R
import com.hanium.floty.model.Diary
import kotlinx.android.synthetic.main.fragment_diary_detail.*

class DiaryDetailFragment : Fragment() {

    lateinit var mReference: DatabaseReference
    lateinit var diaryid: String
    lateinit var firebaseUser: FirebaseUser

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.fragment_diary_detail, container, false)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        val preferences: SharedPreferences = context!!.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        diaryid = preferences.getString("diaryid", "none").toString()

        var edit: RelativeLayout = view.findViewById(R.id.edit)
        var delete: RelativeLayout = view.findViewById(R.id.delete)
        var diaryImage: ImageView = view.findViewById(R.id.diary_img)
        diaryImage.visibility = View.GONE

        var bundle = Bundle()
        var year = arguments!!.getInt("year")
        var month = arguments!!.getInt("month")
        var day = arguments!!.getInt("day")
        var year1 = year
        var month1 = month
        var day1 = day
        var id = diaryid

        edit.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_container, WriteDiaryFragment().apply {
                arguments = bundle.apply {
                    putInt("year", year1)
                    putInt("month", month1)
                    putInt("day", day1)
                    putString("id", id)
                }
            }).addToBackStack(null).commit()
        }

        delete.setOnClickListener {
            FirebaseDatabase.getInstance().getReference("Diary").child(diaryid).removeValue()
            activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_container, CalendarFragment()).addToBackStack(null).commit()
        }

        diaryInfo()

        return view
    }

    private fun diaryInfo() {
        mReference = FirebaseDatabase.getInstance().getReference("Diary").child(diaryid)

        mReference.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val diary: Diary? = dataSnapshot.getValue(Diary::class.java)
                diary?.let {
                    date.text = diary.year + ". " + diary.month + ". " + diary.day
                    diary_title.text = diary.title
                    diary_content.text = diary.description
                    if (diary.image == "null") {
                        diary_img.visibility = View.GONE
                    } else {
                        diary_img.visibility = View.VISIBLE
                        Glide.with(context!!).load(diary.image).into(diary_img)
                    }

                    if (diary.weather == "sunny") {
                        image.setImageResource(R.drawable.ic_sunny)
                    } else if (diary.weather == "cloudy") {
                        image.setImageResource(R.drawable.ic_cloudy)
                    } else if (diary.weather == "snowy") {
                        image.setImageResource(R.drawable.ic_snowy)
                    } else {
                        image.setImageResource(R.drawable.ic_rainy)
                    }
                }
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {

            }
        })
    }

}
