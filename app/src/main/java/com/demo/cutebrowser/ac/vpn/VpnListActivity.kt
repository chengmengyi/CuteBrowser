package com.demo.cutebrowser.ac.vpn

import android.content.Intent
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.cutebrowser.R
import com.demo.cutebrowser.adapter.VpnListAdapter
import com.demo.cutebrowser.admob.LoadAd
import com.demo.cutebrowser.admob.ShowFullAd
import com.demo.cutebrowser.base.BaseActivity
import com.demo.cutebrowser.bean.VpnBean
import com.demo.cutebrowser.conf.CuteConf
import com.demo.cutebrowser.vpn.ConnectVpnManager
import kotlinx.android.synthetic.main.activity_vpn_list.*

class VpnListActivity:BaseActivity() {
    private val show by lazy { ShowFullAd(CuteConf.VPN_BACK,this){finish()} }

    override fun layoutRes(): Int = R.layout.activity_vpn_list

    override fun initView() {
        LoadAd.loadAd(CuteConf.VPN_BACK)
        rv_list.apply {
            layoutManager=LinearLayoutManager(this@VpnListActivity)
            adapter=VpnListAdapter(this@VpnListActivity){ click(it) }
        }
        iv_back.setOnClickListener { onBackPressed() }
    }

    private fun click(vpnBean: VpnBean){
        val server = ConnectVpnManager.server
        val connected = ConnectVpnManager.connected()
        if (connected&&server.csb_ip!=vpnBean.csb_ip){
            showDisconnectDialog {
                back("dis",vpnBean)
            }
        }else{
            if (connected){
                back("",vpnBean)
            }else{
                back("con",vpnBean)
            }
        }
    }

    private fun back(action:String,vpnBean: VpnBean){
        ConnectVpnManager.server=vpnBean
        setResult(1000, Intent().apply {
            putExtra("action",action)
        })
        finish()
    }

    private fun showDisconnectDialog(sure:()->Unit){
        AlertDialog.Builder(this).apply {
            setMessage("You are currently connected and need to disconnect before manually connecting to the server.")
            setPositiveButton("sure") { _, _ ->
                sure.invoke()
            }
            setNegativeButton("cancel",null)
            show()
        }
    }

    override fun onBackPressed() {
        if (null!=LoadAd.getAdBean(CuteConf.VPN_BACK)){
            show.showFullAd {
                if (it){
                    finish()
                }
            }
            return
        }
        finish()
    }
}