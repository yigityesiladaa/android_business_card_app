package com.example.businesscardapp.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.businesscardapp.room.database.AppDatabase
import com.example.businesscardapp.room.database.DatabaseConfig
import com.example.businesscardapp.room.models.Person
import com.example.businesscardapp.room.repositories.PersonRepository

class MainViewModel(application: Application) : AndroidViewModel(application) {

    var getResult = MutableLiveData<Boolean>()
    var person = MutableLiveData<Person?>()
    private val repository : PersonRepository

    init {
        val dao = DatabaseConfig.getDatabase(application).personDao()
        repository = PersonRepository(dao)
    }

    fun getPerson(){
        val result = repository.getById(1)
        person.postValue(result)
        getResult.postValue(true)
    }

}