package com.cutesearching.privatebrowser.util

import android.content.Context
import android.view.View
import android.widget.Toast

fun View.show(show:Boolean){
    visibility=if (show) View.VISIBLE else View.GONE
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