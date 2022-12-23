package com.demo.cutebrowser.ac

import android.animation.ValueAnimator
import android.content.Intent
import android.view.KeyEvent
import android.view.animation.LinearInterpolator
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.blankj.utilcode.util.ActivityUtils
import com.demo.cutebrowser.R
import com.demo.cutebrowser.ac.browser.BrowserActivity
import com.demo.cutebrowser.admob.LoadAd
import com.demo.cutebrowser.admob.ShowFullAd
import com.demo.cutebrowser.base.BaseActivity
import com.demo.cutebrowser.conf.CuteConf
import com.demo.cutebrowser.util.ReloadAdManager
import com.tencent.mmkv.MMKV
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(){
    private lateinit var progressAnimator:ValueAnimator
    private val show by lazy { ShowFullAd(CuteConf.OPEN,this){ toBrowserAc() } }

    override fun layoutRes(): Int = R.layout.activity_main

    override fun initView() {
        readReferrer()
        ReloadAdManager.reset()
        preLoadAd()
        start()
    }

    private fun preLoadAd(){
        LoadAd.loadAd(CuteConf.OPEN)
        LoadAd.loadAd(CuteConf.BOOK_MARK)
        LoadAd.loadAd(CuteConf.HISTORY)
        LoadAd.loadAd(CuteConf.TAB)
        LoadAd.loadAd(CuteConf.VPN_HOME)
        LoadAd.loadAd(CuteConf.HOME)
        LoadAd.loadAd(CuteConf.VPN_CONNECT)
        LoadAd.loadAd(CuteConf.VPN_RESULT)
    }

    private fun start(){
        progressAnimator=ValueAnimator.ofInt(0, 100).apply {
            duration = 10000L
            interpolator = LinearInterpolator()
            addUpdateListener {
                val progress = it.animatedValue as Int
                view_progress.progress = progress
                val time = (10 * (progress / 100.0F)).toInt()
                if (time in 3..9){
                    show.showFullAd { refresh ->
                        stop()
                        view_progress.progress=100
                        if (refresh){
                            toBrowserAc()
                        }
                    }
                }else if (time>=10){
                    toBrowserAc()
                }
            }
            start()
        }
    }

    private fun toBrowserAc(){
//        val activityExistsInStack = ActivityUtils.isActivityExistsInStack(BrowserActivity::class.java)
//        if (!activityExistsInStack){
//            startActivity(Intent(this,BrowserActivity::class.java))
//        }
        startActivity(Intent(this,BrowserActivity::class.java))
        finish()
    }

    private fun stop(){
        progressAnimator.cancel()
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode== KeyEvent.KEYCODE_BACK){
            return true
        }
        return false
    }

    override fun onResume() {
        super.onResume()
        progressAnimator.resume()
    }

    override fun onPause() {
        super.onPause()
        progressAnimator.pause()
    }

    private fun readReferrer(){
        val decodeString = MMKV.defaultMMKV().decodeString("referrer", "")?:""
        if(decodeString.isEmpty()){
            val referrerClient = InstallReferrerClient.newBuilder(application).build()
            referrerClient.startConnection(object : InstallReferrerStateListener {
                override fun onInstallReferrerSetupFinished(responseCode: Int) {
                    try {
                        referrerClient.endConnection()
                        when (responseCode) {
                            InstallReferrerClient.InstallReferrerResponse.OK -> {
                                val installReferrer = referrerClient.installReferrer.installReferrer
                                MMKV.defaultMMKV().encode("referrer",installReferrer)
                            }
                            else->{

                            }
                        }
                    } catch (e: Exception) {

                    }
                }
                override fun onInstallReferrerServiceDisconnected() {
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stop()
    }
}