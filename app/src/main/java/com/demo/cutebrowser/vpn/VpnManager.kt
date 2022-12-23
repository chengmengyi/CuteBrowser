package com.demo.cutebrowser.vpn

import com.demo.cutebrowser.bean.VpnBean
import com.demo.cutebrowser.conf.CuteConf
import com.demo.cutebrowser.conf.CuteFirebase

object VpnManager {

    fun getVpnList()=CuteFirebase.vpnList.ifEmpty { CuteConf.localVpnList }

    fun getFastServer():VpnBean{
        val serverList = getVpnList()
        if (CuteFirebase.cityList.isNullOrEmpty()){
            return serverList.random()
        }else{
            val filter = serverList.filter { CuteFirebase.cityList.contains(it.csb_ccity) }
            if (!filter.isNullOrEmpty()){
                return filter.random()
            }
        }
        return serverList.random()
    }
}