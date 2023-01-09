package com.cutesearching.privatebrowser.admob

import com.tencent.mmkv.MMKV
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

object CuteAdNum {
    var clickMax=15
    var showMax=50
    private var click=0
    private var show=0

    var addTabNum=0

    fun getMax(json:String){
        try {
            val jsonObject = JSONObject(json)
            clickMax=jsonObject.optInt("cute_click")
            showMax=jsonObject.optInt("cute_show")
        }catch (e:Exception){

        }
    }

    fun hasLimit()=click> clickMax||show> showMax

    fun getCurrent(){
        click=MMKV.defaultMMKV().decodeInt(numKey("click"))
        show=MMKV.defaultMMKV().decodeInt(numKey("show"))
    }

    fun saveClick(){
        click++
        MMKV.defaultMMKV().encode(numKey("click"),click)
    }

    fun saveShow(){
        show++
        MMKV.defaultMMKV().encode(numKey("show"),show)
    }

    private fun numKey(type:String): String {
        return try {
            "${type}_${SimpleDateFormat("yyyy-MM-dd").format(Date(System.currentTimeMillis()))}"
        } catch (e: java.lang.Exception) {
            type
        }
    }

    fun checkShowTabAd()=addTabNum%3==0
}