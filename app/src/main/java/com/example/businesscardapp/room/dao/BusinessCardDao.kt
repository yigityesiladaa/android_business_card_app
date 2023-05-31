package com.example.businesscardapp.room.dao

import androidx.room.*
import com.example.businesscardapp.room.models.BusinessCard

@Dao
interface BusinessCardDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert( businessCard: BusinessCard) : Long

    @Query("Select * from businessCard order by id DESC limit 10")
    fun getAll() : List<BusinessCard>

    @Query("Select * from businessCard where group_name like :groupName order by id DESC")
    fun getByGroup(groupName: String) : List<BusinessCard>

    @Query("select * from businessCard where first_name like '%' || :name || '%' or last_name like '%' || :name || '%' ")
    fun searchByName(name: String ) : List<BusinessCard>

    @Query("select * from businessCard where id =:id")
    fun getById(id: Int ) : BusinessCard

    @Query("delete from businessCard where id = :id")
    fun delete(id : Int) : Int

    @Update
    fun update(businessCard: BusinessCard)
}