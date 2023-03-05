package com.nathalie.coffeeapp.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nathalie.coffeeapp.data.service.AuthService
import com.nathalie.coffeeapp.repository.FireStoreDrinkRepository
import com.nathalie.coffeeapp.repository.fireStoreRepo.DrinkRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MyAppDependency {
    @Provides
    @Singleton
    fun getFireStore(): FirebaseFirestore {
        return Firebase.firestore
    }

    @Provides
    @Singleton
    fun getFirebaseAuth(): FirebaseAuth {
        return Firebase.auth
    }

    @Provides
    @Singleton
    fun getFireStoreDrinkRepository(db: FirebaseFirestore): DrinkRepository {
        return FireStoreDrinkRepository(db.collection("drinks"))
    }

    @Provides
    @Singleton
    fun getAuthRepository(auth: FirebaseAuth, db: FirebaseFirestore): AuthService {
        return AuthService(auth, db.collection("users"))
    }
}