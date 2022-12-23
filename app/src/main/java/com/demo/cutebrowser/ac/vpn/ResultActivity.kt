package com.demo.cutebrowser.ac.vpn

import com.demo.cutebrowser.R
import com.demo.cutebrowser.admob.ShowNativeAd
import com.demo.cutebrowser.base.BaseActivity
import com.demo.cutebrowser.conf.CuteConf
import com.demo.cutebrowser.interfaces.IConnectTimeCallback
import com.demo.cutebrowser.util.ReloadAdManager
import com.demo.cutebrowser.util.getVpnLogo
import com.demo.cutebrowser.vpn.ConnectVpnManager
import com.demo.cutebrowser.vpn.TimeManager
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity:BaseActivity(), IConnectTimeCallback {
    private var connect=false
    private val show by lazy { ShowNativeAd(CuteConf.VPN_RESULT,this) }

    override fun layoutRes(): Int = R.layout.activity_result

    override fun initView() {
        iv_back.setOnClickListener { finish() }

        connect=intent.getBooleanExtra("connect",false)
        bg_layout.isSelected=connect
        tv_result.text=if (connect)"Connected" else "Disconnected"
        tv_connect_time.isSelected=connect
        val server = ConnectVpnManager.last
        tv_vpn_name.text=server.csb_ccount
        iv_vpn_logo.setImageResource(getVpnLogo(server.csb_ccount))
        if(connect){
            TimeManager.setCallback(this)
        }else{
            tv_connect_time.text=TimeManager.getMaxTime()
        }
    }

    override fun connectTime(time: String) {
        tv_connect_time.text=time
    }

    override fun onResume() {
        super.onResume()
        if (ReloadAdManager.reload(CuteConf.VPN_RESULT)){
            show.showAd()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        show.stopShow()
        ReloadAdManager.setBool(CuteConf.VPN_RESULT,true)
        if (connect){
            TimeManager.setCallback(null)
        }
    }
}