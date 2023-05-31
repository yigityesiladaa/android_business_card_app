package com.example.businesscardapp.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.businesscardapp.room.models.BusinessCard
import com.example.businesscardapp.room.models.Person
import com.example.businesscardapp.room.dao.BusinessCardDao
import com.example.businesscardapp.room.dao.PersonDao

@Database(entities = [BusinessCard::class, Person::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun businessCardDao() : BusinessCardDao
    abstract fun personDao() : PersonDao

}