package com.cutesearching.privatebrowser.bean

class ResultAdBean(
    val time:Long=0L,
    val ad:Any?=null
) {
    fun notUse()=(System.currentTimeMillis() - time) >=1000L*3600L
}