package com.demo.cutebrowser.util

import android.content.Context
import android.view.View
import android.widget.Toast
import com.demo.cutebrowser.R

fun getVpnLogo(name:String)=when(name){
    "Austria"->R.drawable.aodili
    "United States"->R.drawable.unitedstates
    "United Kingdom"->R.drawable.uk
    else-> R.drawable.fast
}

fun View.show(show:Boolean){
    visibility=if (show) View.VISIBLE else View.GONE
}

fun View.showIn(show:Boolean){
    visibility=if (show) View.VISIBLE else View.INVISIBLE
}

fun View.showInvisible(show:Boolean){
    visibility=if (show) View.VISIBLE else View.INVISIBLE
}

fun Context.funcEnable(){
    showToast("This function is not available on the current page")
}

fun Context.showToast(string: String){
    Toast.makeText(this,string,Toast.LENGTH_SHORT).show()
}


fun str2Int(string: String):Int{
    try {
        return string.toInt()
    }catch (e:Exception){

    }
    return 0
}

fun isBuyUser(referrer:String)=referrer.contains("fb4a")||
        referrer.contains("gclid")||
        referrer.contains("not%20set")||
        referrer.contains("youtubeads")||
        referrer.contains("%7B%22")