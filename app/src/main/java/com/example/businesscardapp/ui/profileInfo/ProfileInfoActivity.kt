package com.example.businesscardapp.ui.profileInfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.businesscardapp.room.database.DatabaseConfig
import com.example.businesscardapp.common.extensions.showProcessSuccessfulSnackBar
import com.example.businesscardapp.common.extensions.showSnackBar
import com.example.businesscardapp.common.utils.Utils
import com.example.businesscardapp.databinding.ActivityProfileInfoBinding
import com.example.businesscardapp.room.models.Person
import com.google.android.material.snackbar.Snackbar

class ProfileInfoActivity : AppCompatActivity() {

    private var _binding : ActivityProfileInfoBinding? = null
    private val binding get() = _binding!!
    private lateinit var profileInfoViewModel: ProfileInfoViewModel
    private var getResult = false
    private var insertResult = false
    private lateinit var timer : CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityProfileInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        registerEvents()
        listenEvents()
    }

    private fun init(){
        timer = Utils.setTimer { onBackPressed() }
        profileInfoViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(application)
        )[ProfileInfoViewModel::class.java]
        Thread{
            profileInfoViewModel.getPerson()
        }.start()
    }

    private fun registerEvents(){
        binding.btnUpdatePersonInfo.setOnClickListener(btnUpdatePersonInfoClickListener)
        binding.btnCancel.setOnClickListener { onBackPressed() }
    }

    private val btnUpdatePersonInfoClickListener = View.OnClickListener{view->
        Thread{
            val person = getPersonModel()
            if(getResult){
                profileInfoViewModel.updatePerson(person)
                runOnUiThread {
                    timer.start()
                    showProcessSuccessfulSnackBar(view, listener = {
                        timer.cancel()
                        timer.onFinish()
                    })
                }
            }else{
                profileInfoViewModel.insertPerson(person)
                runOnUiThread {
                    if(insertResult){
                        timer.start()
                        showProcessSuccessfulSnackBar(view, listener = {
                            timer.cancel()
                            timer.onFinish()
                        })
                    }else{
                        showSnackBar(view,"Person Informations Insert Failed!")
                    }
                }
            }

        }.start()
    }

    private fun getPersonModel() : Person{
        val firstName = binding.etFirstName.text.toString().trim()
        val lastName = binding.etLastName.text.toString().trim()
        val mail = binding.etEmail.text.toString().trim()
        return Person(1,firstName,lastName,mail)
    }

    private fun setEtValues(person: Person?){
        person?.let {
            binding.etFirstName.setText(person.firstName)
            binding.etLastName.setText(person.lastName)
            binding.etEmail.setText(person.mail)
        }
    }

    private fun listenEvents(){
        profileInfoViewModel.person.observe(this){
            setEtValues(it)
        }

        profileInfoViewModel.getResult.observe(this){
            getResult = it
        }

        profileInfoViewModel.insertResult.observe(this){
            insertResult = it
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}