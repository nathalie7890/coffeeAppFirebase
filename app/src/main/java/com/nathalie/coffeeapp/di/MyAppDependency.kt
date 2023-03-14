package com.nathalie.coffeeapp.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nathalie.coffeeapp.data.service.AuthService
import com.nathalie.coffeeapp.repository.FireStoreBeanRepository
import com.nathalie.coffeeapp.repository.FireStoreDrinkRepository
import com.nathalie.coffeeapp.repository.FireStoreRoastRepository
import com.nathalie.coffeeapp.repository.fireStoreRepo.BeanRepository
import com.nathalie.coffeeapp.repository.fireStoreRepo.DrinkRepository
import com.nathalie.coffeeapp.repository.fireStoreRepo.RoastRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

// Application Dependencies to connect to Firebase Services
@Module
@InstallIn(SingletonComponent::class)
object MyAppDependency {

    // Connect to Firebase Firestore Service
    @Provides
    @Singleton
    fun getFireStore(): FirebaseFirestore {
        return Firebase.firestore
    }

    // Connect to Firebase Authentication Service
    @Provides
    @Singleton
    fun getFirebaseAuth(): FirebaseAuth {
        return Firebase.auth
    }

    // Connect to Firebase Firestore "drinks" collection
    @Provides
    @Singleton
    fun getFireStoreDrinkRepository(db: FirebaseFirestore): DrinkRepository {
        return FireStoreDrinkRepository(db.collection("drinks"))
    }

    // Connect to Firebase Firestore "beans" collection
    @Provides
    @Singleton
    fun getFireStoreBeanRepository(db: FirebaseFirestore): BeanRepository {
        return FireStoreBeanRepository(db.collection("beans"))
    }

    // Connect to Firebase Firestore "users" collection
    @Provides
    @Singleton
    fun getAuthRepository(auth: FirebaseAuth, db: FirebaseFirestore): AuthService {
        return AuthService(auth, db.collection("users"))
    }

    // Connect to Firebase Firestore "roasts" collection
    @Provides
    @Singleton
    fun getFireStoreRoastRepository(db: FirebaseFirestore): RoastRepository {
        return FireStoreRoastRepository(db.collection("roasts"))
    }
}