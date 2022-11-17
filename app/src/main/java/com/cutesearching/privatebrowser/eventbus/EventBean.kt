package com.cutesearching.privatebrowser.eventbus

import org.greenrobot.eventbus.EventBus

class EventBean(
    val code:Int,
    var str:String="",
    var int: Int=-1,
    var ob: Any? =null,
) {
    fun send(){
        EventBus.getDefault().post(this)
    }
}