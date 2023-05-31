package com.example.businesscardapp.common.utils

import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class Utils {

    companion object{

        fun setTimer(onFinish: ()-> Unit): CountDownTimer {
            return object : CountDownTimer(2000, 1000) {
                override fun onTick(millisUntilFinished: Long) {}
                override fun onFinish() {
                    onFinish()
                }
            }
        }

        fun editTextChangeListener(et : EditText, afterTextChanged: () -> Unit){
            et.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
                override fun afterTextChanged(s: Editable?) {
                    afterTextChanged()
                }
            })
        }
    }
}