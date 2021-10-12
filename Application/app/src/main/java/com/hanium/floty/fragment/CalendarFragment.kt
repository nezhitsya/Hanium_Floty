package com.hanium.floty.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.hanium.floty.R
import com.hanium.floty.decorator.EventDecorator
import com.hanium.floty.decorator.TodayDecorator
import com.hanium.floty.model.Diary
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import java.text.SimpleDateFormat
import java.util.*

class CalendarFragment : Fragment() {

    lateinit var calendar: MaterialCalendarView
    var dateList = arrayListOf<CalendarDay>()
    var yearList = arrayListOf<String>()
    var monthList = arrayListOf<String>()
    var dayList = arrayListOf<String>()
    lateinit var mReference: DatabaseReference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.fragment_calendar, container, false)

        var todayDate: TextView = view.findViewById(R.id.today_date)
        var todayMonth: TextView = view.findViewById(R.id.today_month)
        var todayYear: TextView = view.findViewById(R.id.today_year)

        var bundle = Bundle()

        var format: SimpleDateFormat = SimpleDateFormat("MMM", Locale.ENGLISH)
        var today: Date = Calendar.getInstance().time

        todayDate.text = CalendarDay.today().day.toString()
        todayYear.text = CalendarDay.today().year.toString()
        todayMonth.text = format.format(today)

        calendar = view.findViewById(R.id.calendar)
        calendar.setSelectedDate(CalendarDay.today())
        calendar.addDecorator(TodayDecorator())

        calendar.setOnDateChangedListener(object: OnDateSelectedListener {
            override fun onDateSelected(widget: MaterialCalendarView, date: CalendarDay, selected: Boolean) {

                if (bundle != null) {
                    activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_container, TodayDiaryFragment().apply {
                        arguments = bundle.apply {
                            putInt("year", date.year)
                            putInt("month", date.month + 1)
                            putInt("day", date.day)
                        }
                    }).addToBackStack(null).commit()
                }
            }
        })

        dateListDeco()

        return view
    }

    private fun dateListDeco() {
        var firebaseUser: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
        mReference = FirebaseDatabase.getInstance().getReference("Diary")

        mReference.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                dateList.clear()
                yearList.clear()
                monthList.clear()
                dayList.clear()

                for(datasanpshot: DataSnapshot in snapshot.children) {
                    val diary: Diary? = datasanpshot.getValue(Diary::class.java)

                    if ((diary?.publisher).equals(firebaseUser.uid)) {
                        if (diary != null) {
                            yearList.add(diary.year.toString())
                            monthList.add((Integer.parseInt(diary.month!!) - 1).toString())
                            dayList.add(diary.day.toString())

                            for (i in 0..(yearList.size - 1)) {
                                dateList.add(CalendarDay.from(Integer.parseInt(yearList[i]), Integer.parseInt(monthList[i]), Integer.parseInt(dayList[i])))
                            }

                            for (j in 0..(dateList.size - 1)) {
                                calendar.addDecorator(EventDecorator(Collections.singleton(dateList[j])))
                            }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

}
