package com.hanium.floty.fragment

import android.os.Bundle
import android.util.Log
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
import com.hanium.floty.adapter.DictionaryAdapter
import com.hanium.floty.model.Plant
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException

class BookmarkFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    lateinit var firebaseUser: FirebaseUser
    lateinit var mReference: DatabaseReference
    var plantList = arrayListOf<Plant>()
    var bookmarkList = arrayListOf<Plant>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View = inflater.inflate(R.layout.fragment_bookmark, container, false)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        try {
            var xpp: XmlPullParser = resources.getXml(R.xml.plant)
            plantList.clear()

            while (xpp.eventType != XmlPullParser.END_DOCUMENT) {
                if (xpp.eventType == XmlPullParser.START_TAG) {
                    if (xpp.name.equals("plant")) {
                        val plant = Plant()

                        plant.plantkor = xpp.getAttributeValue(4)
                        plant.planteng = xpp.getAttributeValue(3)
                        plant.temp = xpp.getAttributeValue(5)
                        plant.hum = xpp.getAttributeValue(1)
                        plant.imgurl = xpp.getAttributeValue(2)
                        plant.detail = xpp.getAttributeValue(0)
                        plant.water= xpp.getAttributeValue(6)
                        plant.waterDetail = xpp.getAttributeValue(7)

                        plantList.add(plant)
                    }
                }
                xpp.next()
            }
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)
        var linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        recyclerView.layoutManager = linearLayoutManager

        bookmarked()

        return view
    }

    private fun bookmarked() {
        mReference = FirebaseDatabase.getInstance().getReference("Bookmark").child(firebaseUser.uid)
        mReference.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                bookmarkList.clear()

                for (i in 0 until plantList.size) {
                    if (snapshot.child(plantList[i].plantkor).exists()) {
                        bookmarkList.add(plantList[i])
                    }
                }

                val adapter = DictionaryAdapter(context!!, bookmarkList)
                recyclerView?.adapter = adapter
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

}