package com.hanium.floty.adapter

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.hanium.floty.MainActivity
import com.hanium.floty.R
import com.hanium.floty.fragment.CommentFragment
import com.hanium.floty.fragment.PostDetailFragment
import com.hanium.floty.model.Post
import com.hanium.floty.model.User
import java.text.DateFormat
import java.text.SimpleDateFormat

class CommunityAdapter(val context: Context, val postList: ArrayList<Post>): RecyclerView.Adapter<CommunityAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityAdapter.Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    inner class Holder(itemView: View?): RecyclerView.ViewHolder(itemView!!) {
        val title = itemView?.findViewById<TextView>(R.id.title)
        var nickname = itemView?.findViewById<TextView>(R.id.nickname)
        var profile = itemView?.findViewById<ImageView>(R.id.profile)
        var time = itemView?.findViewById<TextView>(R.id.time)
        var comment = itemView?.findViewById<Button>(R.id.comment)
        var photo = itemView?.findViewById<ImageView>(R.id.photo)

        fun bind(mPost: Post, context: Context) {
            title?.text = mPost.title
            var df: DateFormat = SimpleDateFormat("yy.MM.dd  hh:mm")
            time?.text = df.format(mPost.time)
            publisherInfo(profile, nickname, mPost.publisher)
            mPost.postid?.let { getComment(it, comment) }

            if (mPost.postimage == "null") {
                photo?.visibility = View.GONE
            } else {
                photo?.visibility = View.VISIBLE
                photo?.let {
                    Glide.with(context).load(mPost.postimage).into(it)
                }
            }

            comment?.setOnClickListener {
                var editor: SharedPreferences.Editor = context.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
                editor.putString("postid", mPost.postid)
                editor.apply()

                val fragment = (context as MainActivity).supportFragmentManager.beginTransaction()
                fragment.replace(R.id.fragment_container, CommentFragment()).addToBackStack(null).commit()
            }

            itemView.setOnClickListener {
                var editor: SharedPreferences.Editor = context.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
                editor.putString("postid", mPost.postid)
                editor.apply()

                val fragment = (context as MainActivity).supportFragmentManager.beginTransaction()
                fragment.replace(R.id.fragment_container, PostDetailFragment()).addToBackStack(null).commit()
            }
        }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bind(postList[position], context)
    }

    private fun publisherInfo(profile: ImageView?, nickname: TextView?, userid: String?) {
        var reference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userid.toString())

        reference.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user: User? = dataSnapshot.getValue(User::class.java)
                if (user != null) {
                    if (nickname != null) {
                        nickname.text = user.nickname
                    }
                    if (profile != null) {
                        Glide.with(context).load(user.profile).into(profile)
                    }
                }
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {

            }
        })
    }

    private fun getComment(postid: String, comment: TextView?) {
        var reference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Comment").child(postid)

        reference.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                comment?.text = ""+dataSnapshot.childrenCount
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {

            }
        })
    }

}