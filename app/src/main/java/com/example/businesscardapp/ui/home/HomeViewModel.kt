package com.example.businesscardapp.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.businesscardapp.room.database.AppDatabase
import com.example.businesscardapp.room.models.BusinessCard
import com.example.businesscardapp.room.repositories.BusinessCardRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    var businessCardList = MutableLiveData<List<BusinessCard>>()
    var getResult = MutableLiveData<Boolean>()
    var repository: BusinessCardRepository? = null


    fun getAll(){
        val result = repository?.getAll()
        result?.let {
            businessCardList.postValue(it)
        }
        getResult.postValue(result != null)

    }

    fun searchByFirstName(text : String){
        val result = repository?.searchByName(text)
        result?.let {
            businessCardList.postValue(it)
        }
        getResult.postValue(result != null)
    }

}