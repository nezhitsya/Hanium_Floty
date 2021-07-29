package com.hanium.floty.fragment

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.hanium.floty.R
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.fragment_write_post.*

class WritePostFragment : Fragment() {

    lateinit var firebaseUser: FirebaseUser
    lateinit var storageRef: StorageReference
    lateinit var reference: DatabaseReference
    lateinit var mImageUri: Uri
    lateinit var postid: String

    var bundle = Bundle()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.fragment_write_post, container, false)

        var write: RelativeLayout = view.findViewById(R.id.write_container)
        var addPhoto: FloatingActionButton = view.findViewById(R.id.addPhoto)

        reference = FirebaseDatabase.getInstance().getReference("Posts")
        storageRef = FirebaseStorage.getInstance().getReference("posts")
        postid = reference.push().key.toString()

        write.setOnClickListener {
            upload()

            activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_container, CommunityFragment().apply {
                arguments = bundle.apply {
                    putString("pageInfo", "Community")
                }
            }).commit()
        }

        addPhoto.setOnClickListener {
            CropImage.activity().setAspectRatio(1, 1).start(this.context as Activity)
        }

        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        storageRef = FirebaseStorage.getInstance().getReference("Posts")

        return view
    }

    private fun getFileExtension(uri: Uri): String? {
        var contentResolver: ContentResolver = context!!.contentResolver
        var mimeTypeMap: MimeTypeMap = MimeTypeMap.getSingleton()

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))
    }

    private fun upload() {
        var description = description.text.toString()
        var title = title.text.toString()

        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["postimage"] = "null"
        hashMap["postid"] = postid
        hashMap["description"] = description
        hashMap["title"] = title
        hashMap["time"] = System.currentTimeMillis()
        hashMap["publisher"] = FirebaseAuth.getInstance().currentUser!!.uid
        reference.child(postid).setValue(hashMap)

        activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_container, CommunityFragment().apply {
            arguments = bundle.apply {
                putString("pageInfo", "Community")
            }
        }).commit()
    }

    private fun uploadPhoto() {
        if(mImageUri != null) {
            val filereference: StorageReference = storageRef.child(getFileExtension(mImageUri).toString())

            var uploadTask: StorageTask<*> = filereference.putFile(mImageUri)
            uploadTask.continueWithTask { task ->
                if(!task.isSuccessful) {

                }
                filereference.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    val url = downloadUri!!.toString()

                    var description = description.text.toString()
                    var title = title.text.toString()

                    val hashMap: HashMap<String, Any> = HashMap()
                    hashMap["postimage"] = url
                    hashMap["postid"] = postid
                    hashMap["description"] = description
                    hashMap["title"] = title
                    hashMap["time"] = System.currentTimeMillis()
                    hashMap["publisher"] = FirebaseAuth.getInstance().currentUser!!.uid
                    reference.child(postid).setValue(hashMap)
                } else {
                    Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener { e ->
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        } else {
            var description = description.text.toString()
            var title = title.text.toString()

            val hashMap: HashMap<String, Any> = HashMap()
            hashMap["postimage"] = "null"
            hashMap["postid"] = postid
            hashMap["description"] = description
            hashMap["title"] = title
            hashMap["time"] = System.currentTimeMillis()
            hashMap["publisher"] = FirebaseAuth.getInstance().currentUser!!.uid
            reference.child(postid).setValue(hashMap)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            var result: CropImage.ActivityResult  = CropImage.getActivityResult(data)

            mImageUri = result.uri
            photo.setImageURI(mImageUri)
        } else {
            Toast.makeText(context, "다시 시도해주세요.", Toast.LENGTH_SHORT).show();
        }
    }

}
