package com.hanium.floty.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

import com.hanium.floty.R
import com.hanium.floty.adapter.CommentAdapter
import com.hanium.floty.model.Comment
import kotlinx.android.synthetic.main.fragment_comment.*

class CommentFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    var commentList = arrayListOf<Comment>()

    lateinit var mReference: DatabaseReference
    lateinit var postid: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.fragment_comment, container, false)

        val preferences: SharedPreferences = context!!.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        postid = preferences.getString("postid", "none").toString()

        var comment: EditText = view.findViewById(R.id.comment)
        var send: TextView = view.findViewById(R.id.send)

        send.setOnClickListener {
            if(comment.text.toString().equals("")) {
                Toast.makeText(context, "댓글을 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                postComment()
            }
        }

        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        recyclerView.layoutManager = linearLayoutManager

        getComment()

        return view
    }

    private fun postComment() {
        var comment = comment.text.toString()
        var reference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Comment").child(postid)
        val commentid: String = reference.push().key.toString()

        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["comment"] = comment
        hashMap["commentid"] = commentid
        hashMap["publisher"] = FirebaseAuth.getInstance().currentUser!!.uid
        hashMap["time"] = System.currentTimeMillis()

        reference.child(commentid).setValue(hashMap)
    }

    private fun getComment() {
        mReference = FirebaseDatabase.getInstance().getReference("Comment").child(postid)

        mReference.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                commentList.clear()

                for(snapshot: DataSnapshot in dataSnapshot.children) {
                    val comment: Comment? = snapshot.getValue(Comment::class.java)
                    comment?.let {
                        commentList.add(comment)
                    }
                }
                val adapter = CommentAdapter(context!!, commentList)
                adapter.notifyDataSetChanged()
                recyclerView.adapter = adapter
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {

            }
        })
    }

}
