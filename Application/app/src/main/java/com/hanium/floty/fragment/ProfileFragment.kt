package com.hanium.floty.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.hanium.floty.EditProfileActivity
import com.hanium.floty.QRActivity

import com.hanium.floty.R
import com.hanium.floty.model.User
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.dday
import kotlinx.android.synthetic.main.fragment_profile.nickname
import kotlinx.android.synthetic.main.fragment_profile.profile
import java.text.SimpleDateFormat
import java.util.*

class ProfileFragment : Fragment() {

    lateinit var firebaseUser: FirebaseUser
    lateinit var mReference: DatabaseReference

    private val ONE_DAY: Int = 24 * 60 * 60 * 1000

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.fragment_profile, container, false)

        var bundle = Bundle()

        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        var logout: TextView = view.findViewById(R.id.logout)
        var resign: TextView = view.findViewById(R.id.resign)
        var setting: ImageView = view.findViewById(R.id.setting)
        var diary: LinearLayout = view.findViewById(R.id.diary)
        var history: LinearLayout = view.findViewById(R.id.history)
        var bookmark: LinearLayout = view.findViewById(R.id.bookmark)
        var community: RelativeLayout = view.findViewById(R.id.community_container)
        var liked: RelativeLayout = view.findViewById(R.id.liked_container)
        var mypost: RelativeLayout = view.findViewById(R.id.mypost_container)

        logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            startActivity(Intent(context!!, QRActivity::class.java))
        }

        resign.setOnClickListener {
            FirebaseAuth.getInstance().currentUser!!.delete().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    FirebaseAuth.getInstance().signOut()

                    startActivity(Intent(context!!, QRActivity::class.java))
                } else {
                    Toast.makeText(context, "다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        setting.setOnClickListener {
            startActivity(Intent(context!!, EditProfileActivity::class.java))
        }

        bookmark.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_container, BookmarkFragment()).addToBackStack(null).commit()
        }

        diary.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_container, DiaryListFragment()).addToBackStack(null).commit()
        }

        history.setOnClickListener{
            activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_container, HistoryFragment()).addToBackStack(null).commit()
        }

        community.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_container, CommunityFragment().apply {
                arguments = bundle.apply {
                    putString("pageInfo", "Community")
                }
            }).addToBackStack(null).commit()
        }

        liked.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_container, CommunityFragment().apply {
                arguments = bundle.apply {
                    putString("pageInfo", "Liked")
                }
            }).addToBackStack(null).commit()
        }

        mypost.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_container, CommunityFragment().apply {
                arguments = bundle.apply {
                    putString("pageInfo", "Mypost")
                }
            }).addToBackStack(null).commit()
        }

        userInfo()

        return view
    }

    private fun userInfo() {
        mReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.uid)

        val userListener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user: User? = snapshot.getValue(User::class.java)

                user?.let {
                    nickname.text = user.nickname
                    Glide.with(context!!).load(user.profile).into(profile)

                    val dateFormat = SimpleDateFormat("yyyyMMdd")
                    var startDate = dateFormat.parse(user.year!! + user.month!! + user.day!!).time
                    var today = Calendar.getInstance().apply {
                        set(Calendar.HOUR_OF_DAY, 0)
                        set(Calendar.MINUTE, 0)
                        set(Calendar.SECOND, 0)
                        set(Calendar.MILLISECOND, 0)
                    }.time.time

                    dday.text = "D + " + ((today - startDate) / ONE_DAY).toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }
        mReference.addValueEventListener(userListener)
    }

}
