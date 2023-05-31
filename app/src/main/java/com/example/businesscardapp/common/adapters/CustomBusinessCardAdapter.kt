package com.example.businesscardapp.common.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.businesscardapp.R
import com.example.businesscardapp.room.models.BusinessCard

class CustomBusinessCardAdapter(private val context: Context)
    : ArrayAdapter<BusinessCard>(context,R.layout.business_card){

    private var businessCards = listOf<BusinessCard>()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val rootView = LayoutInflater.from(context).inflate(R.layout.business_card,null,true)

        val txtFullName = rootView.findViewById<TextView>(R.id.txtFirstName)
        val txtAddress = rootView.findViewById<TextView>(R.id.txtAddress)
        val txtMail = rootView.findViewById<TextView>(R.id.txtMail)
        val txtPhone = rootView.findViewById<TextView>(R.id.txtPhone)
        val txtGroup = rootView.findViewById<TextView>(R.id.txtGroup)

        val businessCard = businessCards[position]

        txtFullName.text = "${businessCard.firstName} ${businessCard.lastName}"
        txtAddress.text = businessCard.address
        txtMail.text = businessCard.mail
        txtPhone.text = businessCard.phone
        txtGroup.text = businessCard.group

        return rootView
    }

    override fun getCount(): Int {
        return businessCards.count()
    }

    fun submitList(list : List<BusinessCard>){
        businessCards = list
        notifyDataSetChanged()
    }

}