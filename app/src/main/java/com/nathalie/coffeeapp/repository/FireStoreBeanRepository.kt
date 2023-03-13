package com.nathalie.coffeeapp.repository

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.rpc.context.AttributeContext.Auth
import com.nathalie.coffeeapp.data.model.fireStoreModel.Bean
import com.nathalie.coffeeapp.data.model.fireStoreModel.Drink
import com.nathalie.coffeeapp.data.service.AuthService
import com.nathalie.coffeeapp.repository.fireStoreRepo.BeanRepository
import kotlinx.coroutines.tasks.await

class FireStoreBeanRepository(
    private val ref: CollectionReference,
) : BeanRepository {

    override suspend fun getAllBeans(uid:String): List<Bean> {
        val beans = mutableListOf<Bean>()
        val res = ref.whereEqualTo("uid", uid).get().await()
        for (document in res) {
            beans.add(document.toObject(Bean::class.java).copy(id = document.id))
        }
        return beans
    }

    override suspend fun getBeanById(id: String): Bean? {
        val res = ref.document(id).get().await()
        return res.toObject(Bean::class.java)?.copy(id = id)
    }

    override suspend fun addBean(bean: Bean) {
        ref.add(bean).await()
    }

    override suspend fun updateBean(id: String, bean: Bean): Bean? {
        val update = bean.copy(id = id)
        ref.document(id).set(update).await()
        return update
    }

    override suspend fun deleteBean(id: String) {
        ref.document(id).delete().await()
    }
}