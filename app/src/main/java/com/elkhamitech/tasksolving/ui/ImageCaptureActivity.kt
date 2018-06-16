package com.elkhamitech.tasksolving.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.etisalat.sampletask.R
import kotlinx.android.synthetic.main.activity_image_capture.*

class ImageCaptureActivity : AppCompatActivity() {

    private val cameraRequestCode = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_capture)

        Glide.with(this)
                .load(R.drawable.ic_image)
                .apply(RequestOptions().centerInside())
                .into(photoImageView)

        addNewImage.setOnClickListener {
            val goToCamera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (goToCamera.resolveActivity(packageManager) != null) {
                startActivityForResult(goToCamera, cameraRequestCode)

            }
        }
    }

    @SuppressLint("ShowToast")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            cameraRequestCode -> {
                if (resultCode == Activity.RESULT_OK && data != null) {

                    Glide.with(this)
                            .load(data.extras.get("data") as Bitmap)
                            .apply(RequestOptions().fitCenter())
                            .into(photoImageView)

                }
            }
            else -> {
                Toast.makeText(this, "Error getting the image.", Toast.LENGTH_SHORT)
            }
        }
    }
}
