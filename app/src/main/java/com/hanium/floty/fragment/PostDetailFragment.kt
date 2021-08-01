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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

import com.hanium.floty.R
import com.hanium.floty.adapter.CommentAdapter
import com.hanium.floty.model.Comment
import com.hanium.floty.model.Post
import com.hanium.floty.model.User
import kotlinx.android.synthetic.main.dictionary_item.*
import kotlinx.android.synthetic.main.fragment_post_detail.*
import java.text.DateFormat
import java.text.SimpleDateFormat

class PostDetailFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    var commentList = arrayListOf<Comment>()

    lateinit var mReference: DatabaseReference
    lateinit var postid: String
    lateinit var firebaseUser: FirebaseUser

    var bundle = Bundle()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.fragment_post_detail, container, false)

        var comment: RelativeLayout = view.findViewById(R.id.comment_container)
        comment.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_container, CommentFragment()).addToBackStack(null).commit()
        }

        var like: ImageView = view.findViewById(R.id.like)
        like.setOnClickListener {
            if (like.tag == "dislike") {
                FirebaseDatabase.getInstance().reference.child("Likes").child(firebaseUser.uid).child(postid).setValue(true)
            } else {
                FirebaseDatabase.getInstance().reference.child("likes").child(firebaseUser.uid).child(postid).removeValue()
            }
        }

        var trash: ImageView = view.findViewById(R.id.delete)
        trash.setOnClickListener {
            FirebaseDatabase.getInstance().getReference("Posts").child(postid).removeValue()
            activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_container, CommunityFragment().apply {
                arguments = bundle.apply {
                    putString("pageInfo", "Mypost")
                }
            }).addToBackStack(null).commit()
        }

        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        val preferences: SharedPreferences = context!!.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        postid = preferences.getString("postid", "none").toString()

        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        recyclerView.layoutManager = linearLayoutManager

        postInfo()
        getComment()
        liked(postid, like)

        return view
    }

    private fun postInfo() {

        mReference = FirebaseDatabase.getInstance().getReference("Posts").child(postid)

        mReference.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val post: Post? = dataSnapshot.getValue(Post::class.java)
                post?.let {
                    title.text = post.title
                    description.text = post.description
                    var df: DateFormat = SimpleDateFormat("yy.MM.dd  hh:mm")
                    time.text = df.format(post.time)

                    var publisher = post.publisher
                    if (publisher != null) {
                        publisherInfo(publisher)
                    }

                    if (publisher == firebaseUser.uid) {
                        delete.visibility = View.VISIBLE
                        like.visibility = View.GONE
                    }
                }
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {

            }
        })
    }

    private fun publisherInfo(publisher: String) {

        mReference = FirebaseDatabase.getInstance().getReference("Users").child(publisher)

        mReference.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user: User? = dataSnapshot.getValue(User::class.java)
                user?.let {
                    nickname.text = user.nickname
                    Glide.with(context!!).load(user.profile).into(profile)
                }
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {

            }
        })
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
                recyclerView.adapter = adapter
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {

            }
        })
    }

    private fun liked(postid: String, imageView: ImageView) {
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        mReference = FirebaseDatabase.getInstance().getReference().child("Likes").child(firebaseUser.uid)

        mReference.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.child(postid).exists()) {
                    imageView.setImageResource(R.drawable.ic_like)
                    imageView.tag = "like"
                } else {
                    imageView.setImageResource(R.drawable.ic_dislike)
                    imageView.tag = "dislike"
                }
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {

            }
        })
    }

}
