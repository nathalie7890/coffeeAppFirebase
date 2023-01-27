package com.nathalie.coffeeapp.repository

import com.nathalie.coffeeapp.data.CoffeeDao
import com.nathalie.coffeeapp.data.model.Bean
import com.nathalie.coffeeapp.data.model.Drink

// class that sends function requests to the DAO queries
class BeanRepository(private val coffeeDao: CoffeeDao) {
    // fetch all Beans
    suspend fun getBeans(): List<Bean> {
        return coffeeDao.getBeans().toList()
    }

    // add new Bean
    suspend fun addBean(bean: Bean) {
        coffeeDao.insertBean(bean)
    }

    // find Bean that match ID
    suspend fun getBeanById(id: Long): Bean? {
        return coffeeDao.getBeanById(id)
    }

    // find Bean by ID and UPDATE
    suspend fun updateBean(id: Long, bean: Bean) {
        coffeeDao.insertBean(bean.copy(id = id))
    }

    // DELETE Bean by ID
    suspend fun deleteBean(id: Long) {
        coffeeDao.deleteBean(id)
    }
}