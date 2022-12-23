package com.demo.cutebrowser.util

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import com.blankj.utilcode.util.ActivityUtils
import com.demo.cutebrowser.ac.MainActivity
import com.demo.cutebrowser.ac.browser.BrowserActivity
import com.google.android.gms.ads.AdActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object ActivityCallback {
    var acIsFront=true
    var checkPlan=true
    //0冷启动 1热启动 2已启动
    var loadAppType=0
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
                        loadAppType=1
                        try {
                            checkPlan=ActivityUtils.getTopActivity().javaClass.name==BrowserActivity::class.java.name
                        }catch (e:Exception){

                        }
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