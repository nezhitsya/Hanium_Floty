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
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_edit_profile.*

class EditProfileActivity : AppCompatActivity() {

    lateinit var mImageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

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
    }

    private fun getFileExtension(uri: Uri): String? {
        var contentResolver: ContentResolver = contentResolver
        var mimeTypeMap: MimeTypeMap = MimeTypeMap.getSingleton()

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))
    }

    private fun uploadImage() {

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
