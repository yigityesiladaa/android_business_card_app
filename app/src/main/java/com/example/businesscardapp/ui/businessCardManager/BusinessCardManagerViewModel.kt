package com.example.businesscardapp.ui.businessCardManager

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.businesscardapp.room.database.DatabaseConfig
import com.example.businesscardapp.room.models.BusinessCard
import com.example.businesscardapp.room.repositories.BusinessCardRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BusinessCardManagerViewModel(application: Application) : AndroidViewModel(application) {

    var businessCard = MutableLiveData<BusinessCard?>()
    var deleteResult = MutableLiveData<Boolean>()
    var insertResult = MutableLiveData<Boolean>()
    private val repository : BusinessCardRepository

    init {
        val dao = DatabaseConfig.getDatabase(application).businessCardDao()
        repository = BusinessCardRepository(dao)
    }

    fun getById(id : Int){
        val result = repository.getById(id)
        businessCard.postValue(result)
    }

    fun update(businessCard: BusinessCard){
        repository.update(businessCard)
    }

    fun delete(id : Int){
        val result = repository.delete(id)
        deleteResult.postValue(result > 0)
    }

    fun insert(businessCard: BusinessCard){
        val result = repository.insert(businessCard)
        insertResult.postValue(result > 0)
    }

}