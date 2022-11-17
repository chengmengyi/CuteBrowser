package com.cutesearching.privatebrowser.interfaces

import android.view.View

interface IBrowserCallback {
    fun changeShowView(view:View)
    fun updateTabSize()
}