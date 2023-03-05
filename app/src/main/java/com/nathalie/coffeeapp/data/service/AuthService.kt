package com.nathalie.coffeeapp.data.service

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.nathalie.coffeeapp.data.model.User
import kotlinx.coroutines.tasks.await

class AuthService(private val auth: FirebaseAuth, private val ref: CollectionReference) {
    suspend fun createUser(user: User) {
        val res = auth.createUserWithEmailAndPassword(user.email, user.pass).await()

        if (res.user != null) {
            ref.document(user.email).set(user)
        }
    }

    suspend fun login(email: String, pass: String): Boolean {
        val res = auth.signInWithEmailAndPassword(email, pass).await()
//        Log.d("debugging", "hello ${res.user?.uid}")
        return res.user?.uid != null
    }

    fun isAuthenticate(): Boolean {
        val user = auth.currentUser
        if (user == null) {
            return false
        }
        return true
    }

    fun deAuthenticate() {
        auth.signOut()
    }

    suspend fun getCurrentUser(): User? {
        return auth.currentUser?.email?.let {
            ref.document(it).get().await().toObject(User::class.java)
        }
    }
}