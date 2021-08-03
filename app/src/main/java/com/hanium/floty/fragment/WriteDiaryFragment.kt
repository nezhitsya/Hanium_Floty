package com.hanium.floty.fragment

import android.app.ActionBar
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

import com.hanium.floty.R
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

        var bundle = Bundle()

        year = arguments!!.getInt("year")
        month = arguments!!.getInt("month")
        day = arguments!!.getInt("day")

        var year1 = year
        var month1 = month
        var day1 = day

        var addPhoto: ImageView = view.findViewById(R.id.addPhoto)
        var save: RelativeLayout = view.findViewById(R.id.save)
        var date: TextView = view.findViewById(R.id.date)
        var sunny: ImageView = view.findViewById(R.id.sunny)
        var cloudy: ImageView = view.findViewById(R.id.cloudy)
        var rainy: ImageView = view.findViewById(R.id.rainy)
        var snowy: ImageView = view.findViewById(R.id.snowy)
        var diaryImage: ImageView = view.findViewById(R.id.diary_img)

        if (mImageUri == null) {
            diaryImage.visibility = View.GONE
        } else {
            diaryImage.visibility = View.VISIBLE
        }

        date.text = year.toString() + ". " + month.toString() + ". " + day.toString()

        addPhoto.setOnClickListener {
            CropImage.activity().setAspectRatio(1, 1).start(context!!, this)
        }

        save.setOnClickListener {
            upload()

            activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_container, TodayDiaryFragment().apply {
                arguments = bundle.apply {
                    putInt("year", year1)
                    putInt("month", month1)
                    putInt("day", day1)
                }
            }).commit()
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

        reference = FirebaseDatabase.getInstance().getReference("Diary")
        storageRef = FirebaseStorage.getInstance().getReference("diary")
        diaryid = reference.push().key.toString()

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
