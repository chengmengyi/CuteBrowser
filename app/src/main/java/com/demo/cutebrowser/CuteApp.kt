package com.demo.cutebrowser

import android.app.Application
import org.litepal.LitePal

class CuteApp:Application() {
    override fun onCreate() {
        super.onCreate()
        LitePal.initialize(this)
    }
}