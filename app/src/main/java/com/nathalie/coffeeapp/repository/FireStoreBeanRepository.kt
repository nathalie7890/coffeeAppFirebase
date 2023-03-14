package com.nathalie.coffeeapp.repository

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.rpc.context.AttributeContext.Auth
import com.nathalie.coffeeapp.data.model.fireStoreModel.Bean
import com.nathalie.coffeeapp.data.model.fireStoreModel.Drink
import com.nathalie.coffeeapp.data.service.AuthService
import com.nathalie.coffeeapp.repository.fireStoreRepo.BeanRepository
import kotlinx.coroutines.tasks.await

// Functions to be used in ViewModels
class FireStoreBeanRepository(
    private val ref: CollectionReference,
) : BeanRepository {

    // Firebase query to get all documents in beans collection
    override suspend fun getAllBeans(uid: String): List<Bean> {
        val beans = mutableListOf<Bean>()
        val res = ref.whereEqualTo("uid", uid).get().await()
        val defaultBeans = ref.whereEqualTo("uid", "default").get().await()

        for (document in res) {
            beans.add(document.toObject(Bean::class.java).copy(id = document.id))
        }

        for (document in defaultBeans) {
            beans.add(document.toObject(Bean::class.java).copy(id = document.id))
        }

        return beans
    }

    // Firebase query to get one document in beans collection
    override suspend fun getBeanById(id: String): Bean? {
        val res = ref.document(id).get().await()
        return res.toObject(Bean::class.java)?.copy(id = id)
    }

    // Firebase query to add one document in beans collection
    override suspend fun addBean(bean: Bean) {
        ref.add(bean).await()
    }

    // Firebase query to edit one document in beans collection
    override suspend fun updateBean(id: String, bean: Bean): Bean? {
        val update = bean.copy(id = id)
        ref.document(id).set(update).await()
        return update
    }

    // Firebase query to delete one document in beans collection
    override suspend fun deleteBean(id: String) {
        ref.document(id).delete().await()
    }
}