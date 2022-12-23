package com.demo.cutebrowser.util

object ReloadAdManager {
    private val map= hashMapOf<String,Boolean>()

    fun reload(type:String)=map[type]?:true

    fun setBool(type: String,boolean: Boolean){
        map[type]=boolean
    }

    fun reset(){
        map.keys.forEach { map[it]=true }
    }
}