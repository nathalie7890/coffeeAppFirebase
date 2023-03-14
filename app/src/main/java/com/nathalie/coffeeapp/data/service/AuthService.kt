package com.nathalie.coffeeapp.data.service

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.nathalie.coffeeapp.data.model.User
import kotlinx.coroutines.tasks.await

// firebase Authentication | User Service
class AuthService(private val auth: FirebaseAuth, private val ref: CollectionReference) {

    // User registration
    suspend fun createUser(user: User) {
        val res = auth.createUserWithEmailAndPassword(user.email, user.pass).await()

        if (res.user != null) {
            ref.document(user.email).set(user)
        }
    }

    // User login
    suspend fun login(email: String, pass: String): Boolean {
        val res = auth.signInWithEmailAndPassword(email, pass).await()
        return res.user?.uid != null
    }

    // Check if logged in
    fun isAuthenticate(): Boolean {
        val user = auth.currentUser
        if (user == null) {
            return false
        }
        return true
    }

    // User logout
    fun deAuthenticate() {
        auth.signOut()
    }

    // Get user ID
    fun getUid(): String? {
        return auth.uid
    }

    // Get logged in user object
    suspend fun getCurrentUser(): User? {
        return auth.currentUser?.email?.let {
            ref.document(it).get().await().toObject(User::class.java)
        }
    }
}