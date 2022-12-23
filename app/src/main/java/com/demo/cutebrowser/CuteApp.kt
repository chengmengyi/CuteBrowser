package com.demo.cutebrowser

import android.app.ActivityManager
import android.app.Application
import com.demo.cutebrowser.ac.browser.BrowserActivity
import com.demo.cutebrowser.conf.CuteFirebase
import com.demo.cutebrowser.util.ActivityCallback
import com.github.shadowsocks.Core
import com.google.android.gms.ads.MobileAds
import com.tencent.mmkv.MMKV
import org.litepal.LitePal

lateinit var cuteApp: CuteApp
class CuteApp:Application() {
    override fun onCreate() {
        super.onCreate()
        cuteApp=this
        Core.init(this,BrowserActivity::class)
        if (!packageName.equals(processName(this))){
            return
        }
        LitePal.initialize(this)
        MobileAds.initialize(this)
        MMKV.initialize(this)
        CuteFirebase.firebase()
        ActivityCallback.callback(this)
        CuteFirebase.checkIR()
    }

    private fun processName(applicationContext: Application): String {
        val pid = android.os.Process.myPid()
        var processName = ""
        val manager = applicationContext.getSystemService(Application.ACTIVITY_SERVICE) as ActivityManager
        for (process in manager.runningAppProcesses) {
            if (process.pid === pid) {
                processName = process.processName
            }
        }
        return processName
    }
}