package com.demo.cutebrowser.vpn

import com.demo.cutebrowser.base.BaseActivity
import com.demo.cutebrowser.bean.VpnBean
import com.demo.cutebrowser.interfaces.IConnectCallback
import com.github.shadowsocks.Core
import com.github.shadowsocks.aidl.IShadowsocksService
import com.github.shadowsocks.aidl.ShadowsocksConnection
import com.github.shadowsocks.bg.BaseService
import com.github.shadowsocks.preference.DataStore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object ConnectVpnManager: ShadowsocksConnection.Callback{
    private var baseActivity:BaseActivity?=null
    private var state=BaseService.State.Stopped
    var server=VpnBean()
    var last=VpnBean()
    private var iConnectCallback:IConnectCallback?=null
    private val sc= ShadowsocksConnection(true)

    fun init(baseActivity: BaseActivity,iConnectCallback: IConnectCallback){
        this.baseActivity=baseActivity
        this.iConnectCallback=iConnectCallback
        sc.connect(baseActivity,this)
    }

    fun connect(){
        state= BaseService.State.Connecting
        GlobalScope.launch {
            if (server.isFast()){
                DataStore.profileId = VpnManager.getFastServer().getVpnId()
            }else{
                DataStore.profileId = server.getVpnId()
            }
            Core.startService()
        }
        TimeManager.resetTime()
    }

    fun disconnect(){
        state= BaseService.State.Stopping
        GlobalScope.launch {
            Core.stopService()
        }
    }

    fun connected()= state==BaseService.State.Connected

    fun disconnected()= state==BaseService.State.Stopped

    override fun stateChanged(state: BaseService.State, profileName: String?, msg: String?) {
        this.state=state
        if (connected()){
            last= server
            TimeManager.start()
        }
        if (disconnected()){
            TimeManager.end()
            iConnectCallback?.disconnectSuccess()
        }
    }

    override fun onServiceConnected(service: IShadowsocksService) {
        val state = BaseService.State.values()[service.state]
        this.state=state
        if (connected()){
            TimeManager.start()
            last= server
            iConnectCallback?.connectSuccess()
        }
    }

    override fun onBinderDied() {
        baseActivity?.let {
            sc.disconnect(it)
        }
    }

    fun onDestroy(){
        onBinderDied()
        baseActivity=null
        iConnectCallback=null
    }
}