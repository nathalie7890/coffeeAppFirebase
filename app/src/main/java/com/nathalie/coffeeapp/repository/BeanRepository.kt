package com.nathalie.coffeeapp.repository

import com.nathalie.coffeeapp.data.CoffeeDao
import com.nathalie.coffeeapp.data.model.Bean
import com.nathalie.coffeeapp.data.model.Drink

class BeanRepository(private val coffeeDao: CoffeeDao) {
    //fetch beans
    suspend fun getBeans(): List<Bean> {
        return coffeeDao.getBeans().toList()
    }

    //add beans
    suspend fun addBean(bean: Bean) {
        coffeeDao.insertBean(bean)
    }

    //find bean that match the given id
    suspend fun getBeanById(id: Long): Bean? {
        return coffeeDao.getBeanById(id)
    }

    //find bean by id and update it
    suspend fun updateBean(id: Long, bean: Bean) {
        coffeeDao.insertBean(bean.copy(id = id))
    }

    //delete bean by id
    suspend fun deleteBean(id: Long) {
        coffeeDao.deleteBean(id)
    }

}