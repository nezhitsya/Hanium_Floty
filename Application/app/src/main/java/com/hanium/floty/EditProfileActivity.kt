package com.hanium.floty

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.MimeTypeMap
import android.widget.ArrayAdapter
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.hanium.floty.model.User
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_edit_profile.*

class EditProfileActivity : AppCompatActivity() {

    lateinit var firebaseUser: FirebaseUser
    lateinit var storageRef: StorageReference
    lateinit var mReference: DatabaseReference
    lateinit var mImageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        mReference = FirebaseDatabase.getInstance().reference.child("Users")
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        storageRef = FirebaseStorage.getInstance().getReference("profile")

        val reference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.uid)
        reference.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val user: User? = snapshot.getValue(User::class.java)
                if (user != null) {
                    nickname.setText(user.nickname)
                    Glide.with(applicationContext).load(user.profile).into(profile)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        ArrayAdapter.createFromResource(
                this,
                R.array.year,
                android.R.layout.simple_spinner_item
        ).also {adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            year.adapter = adapter
        }

        ArrayAdapter.createFromResource(
                this,
                R.array.month,
                android.R.layout.simple_spinner_item
        ).also {adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            month.adapter = adapter
        }

        ArrayAdapter.createFromResource(
                this,
                R.array.day,
                android.R.layout.simple_spinner_item
        ).also {adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            day.adapter = adapter
        }

        profile.setOnClickListener {
            CropImage.activity().setAspectRatio(1, 1).setCropShape(CropImageView.CropShape.OVAL).start(this)
        }

        save.setOnClickListener {
            editProfile(nickname.text.toString())

            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        save_label.setOnClickListener {
            editProfile(nickname.text.toString())

            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun editProfile(nickname: String) {
        val reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.uid)
        val hashMap: HashMap<String, Any> = HashMap()

        var yearSpinner: String = year.selectedItem.toString()
        var monthSpinner: String = month.selectedItem.toString()
        var daySpinner: String = day.selectedItem.toString()

        hashMap["year"] = yearSpinner
        hashMap["month"] = monthSpinner
        hashMap["day"] = daySpinner
        hashMap["nickname"] = nickname
        reference.updateChildren(hashMap)
    }

    private fun getFileExtension(uri: Uri): String? {
        var contentResolver: ContentResolver = contentResolver
        var mimeTypeMap: MimeTypeMap = MimeTypeMap.getSingleton()

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))
    }

    private fun uploadImage() {
        if(mImageUri != null) {
            val filereference: StorageReference = storageRef.child(getFileExtension(mImageUri).toString())
            var uploadTask = filereference.putFile(mImageUri)

            uploadTask.continueWithTask { task ->
                if(!task.isSuccessful) {

                }
                filereference.downloadUrl
            }.addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    val downloadUri = task.result
                    val url = downloadUri!!.toString()

                    val reference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.uid)
                    var hashMap: HashMap<String, Any> = HashMap()

                    hashMap["profile"] = url
                    reference.updateChildren(hashMap)
                }
            }

        } else {
            Toast.makeText(this, "이미지를 선택해주세요.", Toast.LENGTH_SHORT).show();
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            var result: CropImage.ActivityResult  = CropImage.getActivityResult(data)

            mImageUri = result.uri
            uploadImage()
            profile.setImageURI(mImageUri)
        } else {
            Toast.makeText(this, "다시 시도해주세요.", Toast.LENGTH_SHORT).show()
        }
    }
}
