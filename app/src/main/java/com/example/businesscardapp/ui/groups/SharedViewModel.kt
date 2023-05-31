package com.example.businesscardapp.ui.groups

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.businesscardapp.room.database.AppDatabase
import com.example.businesscardapp.room.models.BusinessCard
import com.example.businesscardapp.room.repositories.BusinessCardRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SharedViewModel : ViewModel(){

    var businessCardList = MutableLiveData<List<BusinessCard>>()
    var getResult = MutableLiveData<Boolean>()
    var businessCardRepository : BusinessCardRepository? = null

    fun getByGroup(group : String){
        val result = businessCardRepository?.getByGroup(group)
        result?.let {
            businessCardList.postValue(it)
        }
        getResult.postValue(result != null)
    }
}