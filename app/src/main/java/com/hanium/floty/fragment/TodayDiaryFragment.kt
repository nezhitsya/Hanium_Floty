package com.hanium.floty.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

import com.hanium.floty.R
import com.hanium.floty.adapter.DiaryListAdapter
import com.hanium.floty.model.Diary
import kotlinx.android.synthetic.main.activity_edit_profile.*

class TodayDiaryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    lateinit var mReference: DatabaseReference

    var diaryList = arrayListOf<Diary>()
    var year: Int = 0
    var month: Int = 0
    var day: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.fragment_today_diary, container, false)

        year = arguments!!.getInt("year")
        month = arguments!!.getInt("month")
        day = arguments!!.getInt("day")

        var date: TextView = view.findViewById(R.id.date)

        date.text = year.toString() +". " + month.toString() + ". " + day.toString()

        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        recyclerView.layoutManager = linearLayoutManager

        todayDiary()

        return view
    }

    private fun todayDiary() {
        var firebaseUser: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
        mReference = FirebaseDatabase.getInstance().getReference("Diary")

        mReference.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                diaryList.clear()

                for(datasanpshot: DataSnapshot in snapshot.children) {
                    val diary: Diary? = datasanpshot.getValue(Diary::class.java)

                    if ((diary?.publisher).equals(firebaseUser.uid)) {
                        if ((Integer.parseInt(diary?.year.toString()))?.equals(year)!! && (Integer.parseInt(diary?.month.toString()))?.equals(month)!! && (Integer.parseInt(diary?.day.toString()))?.equals(day)!!) {
                            if (diary != null) {
                                diaryList.add(diary)
                            }
                        }
                    }
                }
                val adapter = DiaryListAdapter(context!!, diaryList)
                adapter.notifyDataSetChanged()
                recyclerView?.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

}
