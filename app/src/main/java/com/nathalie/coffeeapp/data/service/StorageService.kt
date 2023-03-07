package com.nathalie.coffeeapp.data.service

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage

object StorageService {
    private val ref = FirebaseStorage.getInstance().getReference("images/")

    fun addImage(uri: Uri, fileName: String, callback: (status: Boolean) -> Unit) {
        ref.child(fileName).putFile(uri)
            .addOnSuccessListener {
                callback(true)

            }
            .addOnFailureListener {
                callback(false)
            }
    }

    fun getImageUri(fileName: String, callback: (uri: Uri) -> Unit) {
        ref.child(fileName).downloadUrl.addOnSuccessListener {
            callback(it)
        }
    }
}