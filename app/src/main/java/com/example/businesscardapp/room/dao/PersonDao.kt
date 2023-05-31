package com.example.businesscardapp.room.dao

import androidx.room.*
import com.example.businesscardapp.room.models.Person

@Dao
interface PersonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert( person: Person) : Long

    @Query("select * from person where id =:id")
    fun getById(id: Long) : Person

    @Update
    fun update(person: Person)
}