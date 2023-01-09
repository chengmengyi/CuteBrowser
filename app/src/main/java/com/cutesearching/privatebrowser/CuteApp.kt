package com.cutesearching.privatebrowser

import android.app.Application
import com.cutesearching.privatebrowser.conf.CuteFirebase
import com.cutesearching.privatebrowser.util.ActivityCallback
import com.google.android.gms.ads.MobileAds
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.tencent.mmkv.MMKV
import org.litepal.LitePal

lateinit var cuteApp: CuteApp
class CuteApp:Application() {
    override fun onCreate() {
        super.onCreate()
        cuteApp=this
        Firebase.initialize(this)
        MobileAds.initialize(this)
        LitePal.initialize(this)
        MMKV.initialize(this)
        CuteFirebase.firebase()
        ActivityCallback.callback(this)
    }
}