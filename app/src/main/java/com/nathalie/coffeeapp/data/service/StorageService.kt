package com.nathalie.coffeeapp.data.service

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage

// firebase file storage service
object StorageService {
    private val ref = FirebaseStorage.getInstance().getReference("images/")

    // upload file | image to firebase storage
    fun addImage(uri: Uri, fileName: String, callback: (status: Boolean) -> Unit) {
        ref.child(fileName).putFile(uri)
            .addOnSuccessListener {
                callback(true)

            }
            .addOnFailureListener {
                callback(false)
            }
    }

    // get image URI | name | location
    fun getImageUri(fileName: String, callback: (uri: Uri) -> Unit) {
        ref.child(fileName).downloadUrl.addOnSuccessListener {
            callback(it)
        }
    }
}