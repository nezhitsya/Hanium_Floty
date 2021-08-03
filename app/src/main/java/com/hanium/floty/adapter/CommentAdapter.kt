package com.hanium.floty.adapter

import android.content.Context
import android.util.Log
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.hanium.floty.model.Comment
import com.hanium.floty.model.User
import java.text.DateFormat
import java.text.SimpleDateFormat

class CommentAdapter(val context: Context, val commentList: ArrayList<Comment>): RecyclerView.Adapter<CommentAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentAdapter.Holder {
        val view = LayoutInflater.from(parent.context).inflate(com.hanium.floty.R.layout.comment_item, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        Log.d("day", commentList.size.toString())
        return commentList.size
    }

    inner class Holder(itemView: View?): RecyclerView.ViewHolder(itemView!!) {
        var profile = itemView?.findViewById<ImageView>(com.hanium.floty.R.id.profile)
        var nickname = itemView?.findViewById<TextView>(com.hanium.floty.R.id.nickname)
        val comment = itemView?.findViewById<TextView>(com.hanium.floty.R.id.comment)
        var time = itemView?.findViewById<TextView>(com.hanium.floty.R.id.time)
        var option = itemView?.findViewById<Button>(com.hanium.floty.R.id.option)

        fun bind(mComment: Comment, context: Context) {
            comment?.text = mComment.comment
            var df: DateFormat = SimpleDateFormat("yy.MM.dd  hh:mm")
            time?.text = df.format(mComment.time)
            publisherInfo(profile, nickname, mComment.publisher)

            if(mComment.publisher!! != FirebaseAuth.getInstance().currentUser!!.uid) {
                option!!.visibility = View.GONE
            } else {
                option!!.visibility = View.VISIBLE
            }

            option?.setOnClickListener{
                val popupMenu = PopupMenu(context, option, Gravity.END)
                popupMenu.menuInflater.inflate(com.hanium.floty.R.menu.post_menu, popupMenu.menu)
                popupMenu.setOnMenuItemClickListener { item: MenuItem ->
                    when(item.itemId) {
                        com.hanium.floty.R.id.edit -> {
                            Toast.makeText(context, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
                        }
                        com.hanium.floty.R.id.delete -> {
                            FirebaseDatabase.getInstance().getReference("Comment").child(mComment.commentid!!).removeValue().addOnCompleteListener { task ->
                                if(task.isSuccessful) {
                                    Toast.makeText(context, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        else -> {
                            false
                        }
                    } as Boolean
                }
                popupMenu.show()
            }
        }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bind(commentList[position], context)
    }

    private fun publisherInfo(profile: ImageView?, nickname: TextView?, userid: String?) {
        var reference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userid.toString())

        reference.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user: User? = dataSnapshot.getValue(User::class.java)
                if (user != null) {
                    nickname!!.text = user.nickname
                    if (profile != null) {
                        Glide.with(context).load(user.profile).into(profile)
                    }
                }
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {

            }
        })
    }
}