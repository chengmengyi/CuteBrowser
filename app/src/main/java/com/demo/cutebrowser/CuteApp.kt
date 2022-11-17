package com.demo.cutebrowser

import android.app.Application
import com.demo.cutebrowser.conf.CuteFirebase
import com.demo.cutebrowser.util.ActivityCallback
import com.tencent.mmkv.MMKV
import org.litepal.LitePal

lateinit var cuteApp: CuteApp
class CuteApp:Application() {
    override fun onCreate() {
        super.onCreate()
        cuteApp=this
        LitePal.initialize(this)
        MMKV.initialize(this)
        CuteFirebase.firebase()
        ActivityCallback.callback(this)
    }
}