package com.demo.cutebrowser.ac.vpn

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.VpnService
import androidx.appcompat.app.AlertDialog
import com.demo.cutebrowser.R
import com.demo.cutebrowser.admob.LoadAd
import com.demo.cutebrowser.admob.ShowFullAd
import com.demo.cutebrowser.admob.ShowNativeAd
import com.demo.cutebrowser.base.BaseActivity
import com.demo.cutebrowser.conf.CuteConf
import com.demo.cutebrowser.conf.CuteFirebase
import com.demo.cutebrowser.interfaces.IConnectCallback
import com.demo.cutebrowser.util.*
import com.demo.cutebrowser.vpn.ConnectVpnManager
import com.github.shadowsocks.utils.StartService
import kotlinx.android.synthetic.main.activity_vpn_home.*
import kotlinx.coroutines.*

class VpnHomeActivity:BaseActivity(), IConnectCallback {
    private var click=true
    private var permission=false
    private var connect=true
    private var autoConnect=false
    private var checkConnectJob: Job?=null
    private val registerResult=registerForActivityResult(StartService()) {
        if (!it && permission) {
            permission = false
            PointManager.point("cute_vpnget")
            startConnect()
        } else {
            click=true
            PointManager.point("cute_vpnfaile")
            showToast("Connected fail")
        }
    }

    private val showHome by lazy { ShowNativeAd(CuteConf.VPN_HOME,this) }
    private val showConnect by lazy { ShowFullAd(CuteConf.VPN_CONNECT,this){ toResultPage() } }

    override fun layoutRes(): Int = R.layout.activity_vpn_home

    override fun initView() {
        ConnectVpnManager.init(this,this)

        iv_connect_btn.setOnClickListener {
            if (click){
                startActivity(Intent(this,ResultActivity::class.java))
            }
        }

        llc_choose_vpn.setOnClickListener {
            if (click){
                startActivityForResult(Intent(this,VpnListActivity::class.java),1000)
            }
        }

        iv_connect_btn.setOnClickListener {
            if (click){
                click=false
                doClickConnectBtn()
            }
        }
        iv_back.setOnClickListener { if (click)finish() }

        iv_connect_idle.setOnClickListener { iv_connect_btn.performClick() }

        autoConnect=intent.getBooleanExtra("autoConnect",false)
        if(autoConnect){
            iv_connect_btn.performClick()
        }
    }

    private fun doClickConnectBtn(){
        LoadAd.loadAd(CuteConf.VPN_CONNECT)
        LoadAd.loadAd(CuteConf.VPN_RESULT)

        val connected = ConnectVpnManager.connected()
        if (connected){
            updateDisconnectingUI()
            checkConnectResult(false)
        }else{
            if (!autoConnect){
                PointManager.point("cute_vpnfc_click")
            }

            updateVpnInfo()
            if (getNetStatus()==1){
                PointManager.point("cute_vpnfaile")
                showNoNetDialog()
                click=true
                return
            }
            if (VpnService.prepare(this) != null) {
                permission = true
                registerResult.launch(null)
                return
            }
            startConnect()
        }
    }

    private fun startConnect(){
        PointManager.point("cute_vpnfc_conn")
        updateConnectingUI()
        checkConnectResult(true)
    }

    private fun checkConnectResult(connect:Boolean){
        this.connect=connect
        checkConnectJob= GlobalScope.launch(Dispatchers.Main) {
            var time = 0
            while (true) {
                if (!isActive) {
                    break
                }
                delay(1000)
                time++
                if (time==3){
                    if (connect){
                        ConnectVpnManager.connect()
                    }else{
                        ConnectVpnManager.disconnect()
                    }                }
                if (time in 3..9){
                    if(connectDisSuccess()){
                        showConnect.showFullAd { to->
                            cancel()
                            checkConnectFinish(to=to)
                        }
                    }
                }

                if (time >= 10) {
                    cancel()
                    checkConnectFinish()
                }
            }
        }
    }

    private fun connectDisSuccess()=if (connect) ConnectVpnManager.connected() else ConnectVpnManager.disconnected()

    private fun checkConnectFinish(to:Boolean=true){
        if (connectDisSuccess()){
            if (connect){
                PointManager.point("cute_vpnsucc")
                if(autoConnect&&CuteFirebase.csb_test=="B"){
                    LoadAd.removeAll()
                }
                updateConnectedUI()
            }else{
                updateStoppedUI()
                updateVpnInfo()
            }
            if (to){
                toResultPage()
            }
        }else{
            PointManager.point("cute_vpnfaile")
            updateStoppedUI()
            showToast(if (connect) "Connect Fail" else "Disconnect Fail")
        }
        click=true
    }

    private fun toResultPage(){
        if (ActivityCallback.acIsFront){
            startActivity(Intent(this,ResultActivity::class.java).apply {
                putExtra("connect",connect)
            })
        }
    }

    private fun updateConnectedUI(){
        iv_connect_btn.isSelected=true
        tv_connect_text.text="Connected"
        iv_connect_idle.showIn(true)
        lottie_view.show(false)
        iv_connect_idle.setImageResource(R.drawable.vpn_connected)
    }

    private fun updateStoppedUI(){
        iv_connect_btn.isSelected=false
        tv_connect_text.text="Connect"
        iv_connect_idle.showIn(true)
        lottie_view.show(false)
        iv_connect_idle.setImageResource(R.drawable.vpn_idle)
    }

    private fun updateConnectingUI(){
        iv_connect_btn.isSelected=false
        tv_connect_text.text="Connecting"
        iv_connect_idle.showIn(false)
        lottie_view.show(true)
        iv_connect_idle.setImageResource(R.drawable.vpn_idle)
    }

    private fun updateDisconnectingUI(){
        iv_connect_btn.isSelected=false
        tv_connect_text.text="Disconnecting"
        iv_connect_idle.showIn(false)
        lottie_view.show(true)
        iv_connect_idle.setImageResource(R.drawable.vpn_idle)
    }

    private fun updateVpnInfo(){
        val server = ConnectVpnManager.server
        tv_vpn_name.text=server.csb_ccount
        iv_vpn_logo.setImageResource(getVpnLogo(server.csb_ccount))
    }

    override fun connectSuccess() {
        updateConnectedUI()
    }

    override fun disconnectSuccess() {
        if (click){
            updateStoppedUI()
        }
    }

    private fun showNoNetDialog(){
        AlertDialog.Builder(this).apply {
            setMessage("You are not currently connected to the network")
            setPositiveButton("sure", null)
            show()
        }
    }

    private fun getNetStatus(): Int {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
            if (activeNetworkInfo.type == ConnectivityManager.TYPE_WIFI) {
                return 2
            } else if (activeNetworkInfo.type == ConnectivityManager.TYPE_MOBILE) {
                return 0
            }
        } else {
            return 1
        }
        return 1
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode==1000){
            when(data?.getStringExtra("action")){
                "dis"->{
                    iv_connect_btn.performClick()
                }
                "con"->{
                    updateVpnInfo()
                    iv_connect_btn.performClick()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if(ReloadAdManager.reload(CuteConf.VPN_HOME)){
            showHome.showAd()
        }
    }

    override fun onBackPressed() {
        if(click){finish()}
    }

    override fun onDestroy() {
        super.onDestroy()
        checkConnectJob?.cancel()
        checkConnectJob=null
        showHome.stopShow()
        ConnectVpnManager.onDestroy()
        ReloadAdManager.setBool(CuteConf.VPN_HOME,true)

    }
}