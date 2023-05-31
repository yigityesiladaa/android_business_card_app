package com.example.businesscardapp.room.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "businessCard")
data class BusinessCard(
    @PrimaryKey(autoGenerate = true) val id : Int?,
    @ColumnInfo("first_name") val firstName : String?,
    @ColumnInfo("last_name") val lastName : String?,
    @ColumnInfo("phone") val phone : String?,
    @ColumnInfo("mail") val mail : String?,
    @ColumnInfo("address") val address : String?,
    @ColumnInfo("group_name") val group: String?,
)
