package com.example.businesscardapp.ui.profileInfo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.businesscardapp.room.database.AppDatabase
import com.example.businesscardapp.room.database.DatabaseConfig
import com.example.businesscardapp.room.models.Person
import com.example.businesscardapp.room.repositories.BusinessCardRepository
import com.example.businesscardapp.room.repositories.PersonRepository

class ProfileInfoViewModel(application: Application) : AndroidViewModel(application) {

    var person = MutableLiveData<Person?>()
    var getResult = MutableLiveData<Boolean>()
    var insertResult = MutableLiveData<Boolean>()
    private val repository : PersonRepository

    init {
        val dao = DatabaseConfig.getDatabase(application).personDao()
        repository = PersonRepository(dao)
    }

    fun getPerson(){
        val result = repository.getById(1)
        person.postValue(result)
        getResult.postValue(result != null)
    }

    fun insertPerson(person: Person){
        val result =  repository.insert(person)
        insertResult.postValue(result != null && result > 0)
    }

    fun updatePerson(person : Person){
        repository.update(person)
    }

}