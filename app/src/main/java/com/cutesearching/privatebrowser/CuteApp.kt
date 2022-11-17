package com.cutesearching.privatebrowser

import android.app.Application
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import org.litepal.LitePal

class CuteApp:Application() {
    override fun onCreate() {
        super.onCreate()
        Firebase.initialize(this)
        LitePal.initialize(this)
    }
}