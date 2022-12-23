package com.demo.cutebrowser.admob

import com.demo.cutebrowser.admob.CuteAdNum
import com.demo.cutebrowser.admob.LoadAd
import com.demo.cutebrowser.base.BaseActivity
import com.demo.cutebrowser.conf.CuteConf
import com.demo.cutebrowser.util.cuteLog
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.interstitial.InterstitialAd
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ShowFullAd(
    private val t:String,
    private val baseActivity: BaseActivity,
    private val finish:()->Unit
) {

    fun showFullAd(result:(refresh:Boolean)->Unit){
        val adBean = LoadAd.getAdBean(t)
        if(adBean==null&&CuteAdNum.hasLimit()){
            result.invoke(true)
            return
        }
        if (adBean!=null){
            result.invoke(false)
            if(LoadAd.showingFull||!baseActivity.onResume){
                return
            }
            cuteLog("start show $t ad ")
            when(adBean){
                is AppOpenAd->{
                    adBean.fullScreenContentCallback=callback
                    adBean.show(baseActivity)
                }
                is InterstitialAd->{
                    adBean.fullScreenContentCallback=callback
                    adBean.show(baseActivity)
                }
            }
        }
    }

    private val callback=object : FullScreenContentCallback() {
        override fun onAdDismissedFullScreenContent() {
            super.onAdDismissedFullScreenContent()
            LoadAd.showingFull=false
            showFinish()
        }

        override fun onAdShowedFullScreenContent() {
            super.onAdShowedFullScreenContent()
            LoadAd.showingFull=true
            CuteAdNum.saveShow()
            LoadAd.deleteAdBean(t)
        }

        override fun onAdFailedToShowFullScreenContent(p0: AdError) {
            super.onAdFailedToShowFullScreenContent(p0)
            LoadAd.showingFull=false
            LoadAd.deleteAdBean(t)
            showFinish()
        }

        override fun onAdClicked() {
            super.onAdClicked()
            CuteAdNum.saveClick()
        }

        private fun showFinish(){
            if (t!=CuteConf.OPEN||t!=CuteConf.VPN_BACK){
                LoadAd.loadAd(t)
            }
            GlobalScope.launch(Dispatchers.Main) {
                delay(200L)
                if (baseActivity.onResume){
                    finish.invoke()
                }
            }
        }
    }
}