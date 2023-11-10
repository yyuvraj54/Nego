package com.example.nego.firebase

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.util.UUID

class FirebaseStorage() {

    private val storageRef = FirebaseStorage.getInstance().reference
    private val defaultImagePath = "/default/profileImage"

    interface OnImageUploadListener {
        fun onImageUploadSuccess(downloadUrl: String)
        fun onImageUploadFailure(exception: Exception)
    }

    fun uploadImage(selectedImageUrl: Uri, uploadPath: String, listener: OnImageUploadListener) {

        val imagesRef: StorageReference = storageRef.child(uploadPath)

        // Create a reference to the image file
        val imageFileName = UUID.randomUUID().toString()
        val imageRef = imagesRef.child("$imageFileName.jpg")

        // Upload the image to Firebase Storage

        val uploadTask = imageRef.putFile(selectedImageUrl)

        // Handle the upload success or failure
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let { throw it }
            }
            imageRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUrl = task.result.toString()
                listener.onImageUploadSuccess(downloadUrl)
            } else {
                listener.onImageUploadFailure(task.exception!!)
            }
        }
    }

    interface OnImageDownloadListener {
        fun onImageDownloaded(imageUrl: String)
        fun onDownloadFailed(exception: Exception)
    }
    fun getDefaultImage(listener: OnImageDownloadListener){

        val imageRef: StorageReference = storageRef.child(defaultImagePath)

        imageRef.downloadUrl.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val imageUrl = task.result.toString()
                listener.onImageDownloaded(imageUrl)
            } else {
                listener.onDownloadFailed(task.exception!!)
            }
        }
    }
}