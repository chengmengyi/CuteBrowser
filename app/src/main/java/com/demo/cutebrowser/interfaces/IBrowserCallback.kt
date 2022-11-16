package com.demo.cutebrowser.interfaces

import android.view.View

interface IBrowserCallback {
    fun changeShowView(view:View)
    fun updateTabSize()
}