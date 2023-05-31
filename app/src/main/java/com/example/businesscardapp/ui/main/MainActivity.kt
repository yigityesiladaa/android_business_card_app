package com.example.businesscardapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.example.businesscardapp.R
import com.example.businesscardapp.room.database.DatabaseConfig
import com.example.businesscardapp.databinding.ActivityMainBinding
import com.example.businesscardapp.room.models.Person
import com.example.businesscardapp.ui.businessCardManager.BusinessCardManagerActivity
import com.example.businesscardapp.ui.profileInfo.ProfileInfoActivity

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var navView: NavigationView
    private lateinit var headView: View
    private var getResult = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(binding.root)

        //Define ViewModel
        mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(application)
        )[MainViewModel::class.java]
        //Get Person If It Exist
        getPerson()
        //Listen View Model Fields
        listenEvents()

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { view ->
            val intent = Intent(this,BusinessCardManagerActivity::class.java)
            startActivity(intent)
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout

        navView= binding.navView
        headView = navView.getHeaderView(0)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        btnEditProfileClickListener()
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_family, R.id.nav_school, R.id.nav_social, R.id.nav_work
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun getPerson(){
        Thread{
            mainViewModel.getPerson()
        }.start()
    }

    private fun setPersonValues(person : Person?){
        val txtName = navView.findViewById<TextView>(R.id.txtName)
        val txtMail = navView.findViewById<TextView>(R.id.txtEmail)
        if(person != null){
            txtName.text = "${person.firstName} ${person.lastName}"
            txtMail.text = person.mail
        }else{
            txtName.text = "Unknown Name"
            txtMail.text = "Unknown Mail"
        }
    }

    private fun listenEvents(){
        mainViewModel.person.observe(this){
            setPersonValues(it)
        }

        mainViewModel.getResult.observe(this){
            getResult = it
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun btnEditProfileClickListener(){
        val btnEdit = headView.findViewById<Button>(R.id.btnEditProfile)
        btnEdit.setOnClickListener {
            val intent = Intent(this@MainActivity,ProfileInfoActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        getPerson()
    }
}