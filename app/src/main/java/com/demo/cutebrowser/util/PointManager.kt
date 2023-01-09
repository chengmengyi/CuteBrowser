package com.demo.cutebrowser.util

import android.os.Bundle
import com.demo.cutebrowser.conf.CuteFirebase
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

object PointManager {
//    private val remoteConfig= Firebase.analytics

    fun setUser(plan:String){
        cuteLog("point===${plan}")
//        remoteConfig.setUserProperty("cute_user",plan)
    }

    fun point(name:String){
        cuteLog("point===$name")
//        remoteConfig.logEvent(name, Bundle())
    }
}