package com.hanium.floty.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

import com.hanium.floty.R
import com.hanium.floty.adapter.CommunityAdapter
import com.hanium.floty.model.Post
import kotlinx.android.synthetic.main.fragment_community.*

class CommunityFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    lateinit var mReference: DatabaseReference
    var postList = arrayListOf<Post>()
    var likedList = arrayListOf<String>()

    lateinit var pageName: TextView
    lateinit var write: RelativeLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view: View =  inflater.inflate(R.layout.fragment_community, container, false)

        write = view.findViewById(R.id.write_container)
        pageName = view.findViewById(R.id.page_name)
        var pageInfo = arguments!!.getString("pageInfo")

        when (pageInfo) {
            "Community" -> postInfo()
            "Liked" -> getLiked()
            "Mypost" -> getMyPost()
        }

        write.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_container, WritePostFragment()).addToBackStack(null).commit()
        }

        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        recyclerView.layoutManager = linearLayoutManager

        return view
    }

    fun postInfo() {
        pageName.text = "Community"

        mReference = FirebaseDatabase.getInstance().getReference("Posts")
        mReference.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                postList.clear()

                for (dataSnapshot: DataSnapshot in snapshot.children) {
                    var post: Post? = dataSnapshot.getValue(Post::class.java)
                    post?.let {
                        postList?.add(post)
                    }
                }

                val adapter = CommunityAdapter(context!!, postList)
                adapter.notifyDataSetChanged()
                recyclerView?.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun getLiked() {
        pageName.text = "Liked Post"
        write.visibility = View.GONE
        likedList = ArrayList()

        var firebaseUser: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
        mReference = FirebaseDatabase.getInstance().getReference().child("Likes").child(firebaseUser.uid)

        mReference.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot: DataSnapshot in snapshot.children) {
                    dataSnapshot.key?.let { likedList.add(it) }
                }
                readLiked()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun readLiked() {
        mReference = FirebaseDatabase.getInstance().getReference("Posts")

        mReference.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                postList.clear()

                for (dataSnapshot: DataSnapshot in snapshot.children) {
                    val post: Post? = dataSnapshot.getValue(Post::class.java)

                    for (id: String in likedList) {
                        if ((post?.postid).equals(id)) {
                            postList?.add(post!!)
                        }
                    }

                    val adapter = CommunityAdapter(context!!, postList)
                    adapter.notifyDataSetChanged()
                    recyclerView?.adapter = adapter
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun getMyPost() {
        pageName.text = "My Post"
        write.visibility = View.GONE

        var firebaseUser: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
        mReference = FirebaseDatabase.getInstance().getReference("Posts")

        mReference.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                postList.clear()

                for (dataSnapshot: DataSnapshot in snapshot.children) {
                    val post: Post? = dataSnapshot.getValue(Post::class.java)

                    if ((post?.publisher).equals(firebaseUser.uid)) {
                        postList?.add(post!!)
                    }
                }

                val adapter = CommunityAdapter(context!!, postList)
                adapter.notifyDataSetChanged()
                recyclerView?.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

}
