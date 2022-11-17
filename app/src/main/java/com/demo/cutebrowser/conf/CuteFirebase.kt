package com.demo.cutebrowser.conf

import com.demo.cutebrowser.admob.CuteAdNum
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.tencent.mmkv.MMKV

object CuteFirebase {
    fun firebase(){
        CuteAdNum.getCurrent()

//        val remoteConfig = Firebase.remoteConfig
//        remoteConfig.fetchAndActivate().addOnCompleteListener {
//            if (it.isSuccessful){
//                parseAdJson(remoteConfig.getString("cute_ad"))
//            }
//        }
    }

    private fun parseAdJson(json:String){
        CuteAdNum.getMax(json)
        MMKV.defaultMMKV().encode("cute_ad",json)
    }

    fun getStorageAd():String{
        val cuteAd = MMKV.defaultMMKV().decodeString("cute_ad")
        return if (cuteAd.isNullOrEmpty()){
            CuteConf.CUTE_AD
        }else{
            cuteAd
        }
    }
}