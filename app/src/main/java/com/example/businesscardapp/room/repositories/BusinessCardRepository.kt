package com.example.businesscardapp.room.repositories

import com.example.businesscardapp.room.dao.BusinessCardDao
import com.example.businesscardapp.room.models.BusinessCard

class BusinessCardRepository(private val businessCardDao: BusinessCardDao) {

    fun insert(businessCard: BusinessCard) = businessCardDao.insert(businessCard)

    fun getAll() = businessCardDao.getAll()

    fun getByGroup(groupName : String) = businessCardDao.getByGroup(groupName)

    fun searchByName(name : String) = businessCardDao.searchByName(name)

    fun getById(id : Int) = businessCardDao.getById(id)

    fun delete(id : Int) = businessCardDao.delete(id)

    fun update(businessCard: BusinessCard) = businessCardDao.update(businessCard)
}