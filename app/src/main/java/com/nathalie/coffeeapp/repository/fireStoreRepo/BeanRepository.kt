package com.nathalie.coffeeapp.repository.fireStoreRepo

import com.nathalie.coffeeapp.data.model.fireStoreModel.Bean

interface BeanRepository {
    suspend fun getAllBeans(): List<Bean>
    suspend fun getBeanById(id: String): Bean?
    suspend fun addBean(bean: Bean)
    suspend fun updateBean(id: String, bean: Bean): Bean?
    suspend fun deleteBean(id: String)
}