package com.demo.cutebrowser.vpn

import com.demo.cutebrowser.interfaces.IConnectTimeCallback
import kotlinx.coroutines.*
import java.lang.Exception

object TimeManager {
    private var time=0L
    private var timeJob:Job?=null
    private var iConnectTimeCallback:IConnectTimeCallback?=null

    fun setCallback(iConnectTimeCallback:IConnectTimeCallback?){
        this.iConnectTimeCallback=iConnectTimeCallback
    }

    fun resetTime(){
        time=0L
    }

    fun start(){
        if (null!= timeJob) return
        timeJob = GlobalScope.launch(Dispatchers.Main) {
            while (true) {
                iConnectTimeCallback?.connectTime(transTime(time))
                time++
                delay(1000L)
            }
        }
    }

    fun end(){
        timeJob?.cancel()
        timeJob=null
    }

    fun getMaxTime()= transTime(time)

    private fun transTime(t:Long):String{
        try {
            val shi=t/3600
            val fen= (t % 3600) / 60
            val miao= (t % 3600) % 60
            val s=if (shi<10) "0${shi}" else shi
            val f=if (fen<10) "0${fen}" else fen
            val m=if (miao<10) "0${miao}" else miao
            return "${s}:${f}:${m}"
        }catch (e: Exception){}
        return "00:00:00"
    }
}
