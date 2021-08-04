package com.hanium.floty.fragment

import android.app.ActionBar
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.*
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

import com.hanium.floty.R
import com.hanium.floty.model.Diary
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.fragment_write_diary.*
import kotlinx.android.synthetic.main.fragment_write_post.*

class WriteDiaryFragment : Fragment() {

    lateinit var firebaseUser: FirebaseUser
    lateinit var storageRef: StorageReference
    lateinit var reference: DatabaseReference
    lateinit var diaryid: String
    var mImageUri: Uri? = null

    var year: Int = 0
    var month: Int = 0
    var day: Int = 0
    var weather: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.fragment_write_diary, container, false)

        reference = FirebaseDatabase.getInstance().getReference("Diary")
        storageRef = FirebaseStorage.getInstance().getReference("diary")

        var bundle = Bundle()

        year = arguments!!.getInt("year")
        month = arguments!!.getInt("month")
        day = arguments!!.getInt("day")

        var addPhoto: ImageView = view.findViewById(R.id.addPhoto)
        var date: TextView = view.findViewById(R.id.date)
        var sunny: ImageView = view.findViewById(R.id.sunny)
        var cloudy: ImageView = view.findViewById(R.id.cloudy)
        var rainy: ImageView = view.findViewById(R.id.rainy)
        var snowy: ImageView = view.findViewById(R.id.snowy)
        var diaryImage: ImageView = view.findViewById(R.id.diary_img)
        var save: RelativeLayout = view.findViewById(R.id.save)

        if (arguments!!.getString("id").toString() != "null") {
            diaryid = arguments?.getString("id").toString()
            checkEdit()
            addPhoto.visibility = View.GONE

            save.setOnClickListener {
                edit()

                var year1 = year
                var month1 = month
                var day1 = day

                activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_container, TodayDiaryFragment().apply {
                    arguments = bundle.apply {
                        putInt("year", year1)
                        putInt("month", month1)
                        putInt("day", day1)
                    }
                }).commit()
            }
        } else {
            diaryid = reference.push().key.toString()
            addPhoto.visibility = View.VISIBLE

            save.setOnClickListener {
                upload()

                var year1 = year
                var month1 = month
                var day1 = day

                activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_container, TodayDiaryFragment().apply {
                    arguments = bundle.apply {
                        putInt("year", year1)
                        putInt("month", month1)
                        putInt("day", day1)
                    }
                }).commit()
            }
        }

        if (mImageUri == null) {
            diaryImage.visibility = View.GONE
        } else {
            diaryImage.visibility = View.VISIBLE
        }

        date.text = year.toString() + ". " + month.toString() + ". " + day.toString()

        addPhoto.setOnClickListener {
            CropImage.activity().setAspectRatio(1, 1).start(context!!, this)
        }

        sunny.setOnClickListener {
            weather = "sunny"
            sunny.setColorFilter(Color.parseColor("#FFBF00"))
            cloudy.setColorFilter(Color.parseColor("#646464"))
            rainy.setColorFilter(Color.parseColor("#646464"))
            snowy.setColorFilter(Color.parseColor("#646464"))
        }
        cloudy.setOnClickListener {
            weather = "cloudy"
            sunny.setColorFilter(Color.parseColor("#646464"))
            cloudy.setColorFilter(Color.parseColor("#ABF0FF"))
            rainy.setColorFilter(Color.parseColor("#646464"))
            snowy.setColorFilter(Color.parseColor("#646464"))
        }
        rainy.setOnClickListener {
            weather = "rainy"
            sunny.setColorFilter(Color.parseColor("#646464"))
            cloudy.setColorFilter(Color.parseColor("#646464"))
            rainy.setColorFilter(Color.parseColor("#2E69AD"))
            snowy.setColorFilter(Color.parseColor("#646464"))
        }
        snowy.setOnClickListener {
            weather = "snowy"
            sunny.setColorFilter(Color.parseColor("#646464"))
            cloudy.setColorFilter(Color.parseColor("#646464"))
            rainy.setColorFilter(Color.parseColor("#646464"))
            snowy.setColorFilter(Color.parseColor("#CDCDCD"))
        }

        return view
    }

    private fun getFileExtension(uri: Uri): String? {
        var contentResolver: ContentResolver = context!!.contentResolver
        var mimeTypeMap: MimeTypeMap = MimeTypeMap.getSingleton()

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))
    }

    private fun upload() {
        if(mImageUri == null) {
            val hashMap: HashMap<String, Any> = HashMap()
            hashMap["image"] = "null"
            hashMap["publisher"] = FirebaseAuth.getInstance().currentUser!!.uid
            hashMap["day"] = day.toString()
            hashMap["year"] = year.toString()
            hashMap["month"] = month.toString()
            hashMap["title"] = diary_title.text.toString()
            hashMap["description"] = diary_content.text.toString()
            hashMap["diaryid"] = diaryid
            hashMap["weather"] = weather
            reference.child(diaryid).updateChildren(hashMap)
        } else {
            val hashMap: HashMap<String, Any> = HashMap()
            hashMap["publisher"] = FirebaseAuth.getInstance().currentUser!!.uid
            hashMap["day"] = day.toString()
            hashMap["year"] = year.toString()
            hashMap["month"] = month.toString()
            hashMap["title"] = diary_title.text.toString()
            hashMap["description"] = diary_content.text.toString()
            hashMap["diaryid"] = diaryid
            hashMap["weather"] = weather
            reference.child(diaryid).updateChildren(hashMap)
        }
    }

    private fun uploadPhoto() {
        if(mImageUri != null) {
            val filereference: StorageReference = storageRef.child(getFileExtension(mImageUri!!).toString())

            var uploadTask = filereference.putFile(mImageUri!!)
            uploadTask.continueWithTask { task ->
                if(!task.isSuccessful) {

                }
                filereference.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    val url = downloadUri!!.toString()

                    val hashMap: HashMap<String, Any> = HashMap()
                    hashMap["image"] = url
                    reference.child(diaryid).updateChildren(hashMap)
                } else {
                    Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener { e ->
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkEdit() {
        val ref: DatabaseReference = reference.child(diaryid)
        ref.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val diary: Diary? = snapshot.getValue(Diary::class.java)
                if (diary != null) {
                    diary_title.setText(diary.title)
                    if (diary.image != "null") {
                        diary_img.visibility = View.VISIBLE
                        Glide.with(context!!).load(diary.image).into(diary_img)
                    } else {
                        diary_img.visibility = View.GONE
                    }
                    diary_content.setText(diary.description)
                    if (diary.weather == "sunny") {
                        sunny.performClick()
                    } else if (diary.weather == "rainy") {
                        rainy.performClick()
                    } else if (diary.weather == "snowy") {
                        snowy.performClick()
                    } else {
                        cloudy.performClick()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun edit() {
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["title"] = diary_title.text.toString()
        hashMap["description"] = diary_content.text.toString()
        hashMap["weather"] = weather
        reference.child(diaryid).updateChildren(hashMap)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            var result: CropImage.ActivityResult = CropImage.getActivityResult(data)

            mImageUri = result.uri
            diary_img.visibility = View.VISIBLE
            uploadPhoto()
            diary_img.setImageURI(mImageUri)
        } else {
            Toast.makeText(context, "다시 시도해주세요.", Toast.LENGTH_SHORT).show();
        }
    }

}
