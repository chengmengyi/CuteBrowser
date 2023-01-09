package com.cutesearching.privatebrowser.util

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import com.blankj.utilcode.util.ActivityUtils
import com.cutesearching.privatebrowser.ac.MainActivity
import com.cutesearching.privatebrowser.ac.browser.BrowserActivity
import com.google.android.gms.ads.AdActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object ActivityCallback {
    var acIsFront=true
    var refreshNativeAd=true
    private var acIsBack=false
    private var job: Job?=null

    fun callback(application: Application){
        application.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks{
            private var acNum=0
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

            override fun onActivityStarted(activity: Activity) {
                acNum++
                stopJob()
                if (acNum==1){
                    acIsFront=true
                    if (acIsBack){
                        if (ActivityUtils.isActivityExistsInStack(BrowserActivity::class.java)){
                            activity.startActivity(Intent(activity, MainActivity::class.java))
                        }
                    }
                    acIsBack=false
                }
            }

            override fun onActivityResumed(activity: Activity) {}

            override fun onActivityPaused(activity: Activity) {}

            override fun onActivityStopped(activity: Activity) {
                acNum--
                if (acNum<=0){
                    acIsFront=false
                    refreshNativeAd=true
                    job= GlobalScope.launch {
                        delay(3000L)
                        acIsBack=true
                        ActivityUtils.finishActivity(MainActivity::class.java)
                        ActivityUtils.finishActivity(AdActivity::class.java)
                    }
                }
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

            override fun onActivityDestroyed(activity: Activity) {}

            private fun stopJob(){
                job?.cancel()
                job=null
            }
        })
    }
}