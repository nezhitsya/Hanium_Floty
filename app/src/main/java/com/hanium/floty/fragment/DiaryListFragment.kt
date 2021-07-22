package com.hanium.floty.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

import com.hanium.floty.R
import com.hanium.floty.adapter.DiaryListAdapter
import com.hanium.floty.model.Diary

class DiaryListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    var diaryList = arrayListOf<Diary>()

    lateinit var mReference: DatabaseReference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.fragment_diary_list, container, false)

        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        recyclerView.layoutManager = linearLayoutManager

        diaryList()

        return view
    }

    private fun diaryList() {
        var firebaseUser: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
        mReference = FirebaseDatabase.getInstance().getReference("Diary")

        mReference.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                diaryList.clear()

                for(datasanpshot: DataSnapshot in snapshot.children) {
                    val diary: Diary? = datasanpshot.getValue(Diary::class.java)

                    if ((diary?.publisher).equals(firebaseUser.uid)) {
                        diaryList?.add(diary!!)
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
