package com.nathalie.coffeeapp.repository.fireStoreRepo

import com.nathalie.coffeeapp.data.model.fireStoreModel.Bean

// Repository to send functions to Firebase FireStoreBeanRepository
interface BeanRepository {

    // Fetches all documents in beans collection
    suspend fun getAllBeans(uid: String): List<Bean>

    // Fetches one document in beans collection
    suspend fun getBeanById(id: String): Bean?

    // Adds a document in beans collection
    suspend fun addBean(bean: Bean)

    // Edits a document in beans collection
    suspend fun updateBean(id: String, bean: Bean): Bean?

    // Deletes a document in beans collection
    suspend fun deleteBean(id: String)
}