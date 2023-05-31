package com.example.businesscardapp.room.repositories

import com.example.businesscardapp.room.dao.PersonDao
import com.example.businesscardapp.room.models.Person

class PersonRepository(private val personDao: PersonDao) {
    fun insert(person: Person) = personDao.insert(person)
    fun getById(id : Long) = personDao.getById(id)
    fun update(person: Person) = personDao.update(person)
}

