package com.example.businesscardapp.common.extensions

import android.app.Activity
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

fun Activity.showToast(message : String){
    Toast.makeText(this,message, Toast.LENGTH_LONG).show()
}

fun Activity.showSnackBar(view : View, message: String){
    Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
}

fun Activity.showProcessSuccessfulSnackBar(view : View, listener: OnClickListener? = null){
    Snackbar.make(view,"Process successful, you are being redirected to the homepage, please wait..",Snackbar.LENGTH_LONG)
        .setAction("OK",listener)
        .show()
}