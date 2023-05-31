package com.example.businesscardapp.ui.businessCardManager

import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.businesscardapp.R
import com.example.businesscardapp.common.extensions.showProcessSuccessfulSnackBar
import com.example.businesscardapp.common.extensions.showSnackBar
import com.example.businesscardapp.common.extensions.showToast
import com.example.businesscardapp.common.utils.Utils
import com.example.businesscardapp.databinding.ActivityBusinessCardManagerBinding
import com.example.businesscardapp.room.models.BusinessCard

class BusinessCardManagerActivity : AppCompatActivity() {

    private lateinit var binding : ActivityBusinessCardManagerBinding
    private lateinit var viewModel: BusinessCardManagerViewModel
    private var deleteResult = false
    private var insertResult = false
    lateinit var spinnerAdapter : ArrayAdapter<String>
    private var businessCardId : Int? = null
    private var spinnerPosition : Int? = null
    private var groups = arrayOf<String>()
    private lateinit var timer : CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBusinessCardManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        registerEvents()
        listenEvents()
    }

    private fun init(){
        timer = Utils.setTimer { onBackPressed() }
        //if id is sent catch it
        businessCardId = intent.getIntExtra("id",0)
        //Define View Model
        viewModel = ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory(application))[BusinessCardManagerViewModel::class.java]
        //Get Group List From Resources
        groups = resources.getStringArray(R.array.groups)
        //Define Spinner Adapter and Bind it to Spinner
        spinnerAdapter = ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,groups)
        binding.groupSpinner.adapter = spinnerAdapter
        //Get Business Card If It Exist
        getBusinessCard()
    }

    private fun registerEvents(){
        binding.btnSave.setOnClickListener(btnSaveClickListener)
        binding.btnDelete.setOnClickListener(btnDeleteClickListener)
        binding.btnCancel.setOnClickListener { onBackPressed() }
        Utils.editTextChangeListener(binding.etFirstName){textChangeListener(binding.etFirstName.text,binding.txtFirstName)}
        Utils.editTextChangeListener(binding.etLastName){textChangeListener(binding.etLastName.text,binding.txtLastName)}
        Utils.editTextChangeListener(binding.etEmail){textChangeListener(binding.etEmail.text,binding.txtMail)}
        Utils.editTextChangeListener(binding.etPhone){textChangeListener(binding.etPhone.text,binding.txtPhone)}
        Utils.editTextChangeListener(binding.etAddress){textChangeListener(binding.etAddress.text,binding.txtAddress)}
    }

    private fun getBusinessCard(){
        if(businessCardId != null && businessCardId != 0){
            binding.btnDelete.visibility = View.VISIBLE
            Thread{
                viewModel.getById(businessCardId!!)
            }.start()
        }else{
            binding.btnDelete.visibility = View.GONE
        }
    }

    private fun setEtValues(businessCard : BusinessCard){
        binding.etFirstName.setText(businessCard.firstName)
        binding.etLastName.setText(businessCard.lastName)
        binding.etEmail.setText(businessCard.mail)
        binding.etPhone.setText(businessCard.phone)
        binding.etAddress.setText(businessCard.address)
        binding.txtGroup.text = businessCard.group
    }

    private fun setGroupText(){
        binding.groupSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?,position: Int,id: Long) {
                binding.txtGroup.text = groups[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private val btnSaveClickListener = View.OnClickListener {view->
        Thread{
            val businessCard = getBusinessCardModel()
            if(businessCard != null){
                if(businessCardId == 0) insertBusinessCard(businessCard,view) else updateBusinessCard(businessCard,view)
            }else{
               runOnUiThread { showToast("All Fields Are Required!") }
            }
        }.start()
    }

    private fun insertBusinessCard(businessCard: BusinessCard, view: View){
        viewModel.insert(businessCard)
        runOnUiThread {
            if(insertResult){
                timer.start()
                showProcessSuccessfulSnackBar(view, listener = {
                    timer.cancel()
                    timer.onFinish()
                })
            }else{
                showSnackBar(view,"Business Card Insert Failed")
            }
        }
    }

    private fun updateBusinessCard(businessCard: BusinessCard, view: View){
        viewModel.update(businessCard)
        runOnUiThread {
            timer.start()
            showProcessSuccessfulSnackBar(view, listener = {
                timer.cancel()
                timer.onFinish()
            })
        }
    }

    private val btnDeleteClickListener = View.OnClickListener { view->
        Thread{
            businessCardId?.let {
                viewModel.delete(it)
                runOnUiThread {
                    if(deleteResult){
                        timer.start()
                        showProcessSuccessfulSnackBar(view, listener = {
                            timer.cancel()
                            timer.onFinish()
                        })
                    }else{
                        showSnackBar(view,"Business Card Delete Failed")
                    }
                }
            }
        }.start()
    }

    private fun getBusinessCardModel() : BusinessCard? {
        val firstName = binding.txtFirstName.text.toString().trim()
        val lastName = binding.txtLastName.text.toString().trim()
        val mail = binding.etEmail.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val address = binding.etAddress.text.toString().trim()
        val group = binding.groupSpinner.selectedItem.toString()
        return if(firstName.isNotEmpty() && lastName.isNotEmpty() && mail.isNotEmpty() && phone.isNotEmpty() && address.isNotEmpty() && group.isNotEmpty()){
            if(businessCardId != 0) {
                BusinessCard(businessCardId,firstName,lastName,phone,mail,address,group)
            }else{
                BusinessCard(null,firstName,lastName,phone,mail,address,group)
            }
        }else{
            return null
        }
    }

    private fun textChangeListener(s:Editable?, textView: TextView){
        if(s != null){
            textView.text = s.toString().trim()
        }
    }

    private fun listenEvents(){
        viewModel.businessCard.observe(this){result->
           result?.let {bc->
               spinnerPosition = spinnerAdapter.getPosition(bc.group)
               spinnerPosition?.let {
                   binding.groupSpinner.setSelection(it)
                   setGroupText()
               }
               setEtValues(bc)
           }
        }

        viewModel.deleteResult.observe(this){result->
            deleteResult = result
        }

        viewModel.insertResult.observe(this){result->
            insertResult = result
        }
    }

}