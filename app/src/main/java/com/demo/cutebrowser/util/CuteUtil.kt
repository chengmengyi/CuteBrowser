package com.demo.cutebrowser.util

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

fun cuteLog(string: String){
    Log.e("qwer",string)
}

fun formatTime(time:Long):String{
    return try {
        SimpleDateFormat("yyyy-MM-dd").format(Date(time))
    }catch (e:Exception){
        "time error"
    }
}