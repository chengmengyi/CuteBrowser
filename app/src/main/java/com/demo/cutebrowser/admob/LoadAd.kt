package com.demo.cutebrowser.admob

import com.demo.cutebrowser.bean.CuteAdBean
import com.demo.cutebrowser.bean.ResultAdBean
import com.demo.cutebrowser.conf.CuteConf
import com.demo.cutebrowser.cuteApp
import com.demo.cutebrowser.util.cuteLog
import com.demo.cutebrowser.util.getAdList
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAdOptions

object LoadAd {
    var showingFull=false
    private val loading= arrayListOf<String>()
    private val resultAd= hashMapOf<String,ResultAdBean>()

    fun loadAd(t:String,loadAgainOpen:Boolean=true){
        if(CuteAdNum.hasLimit()){
            cuteLog("all limit")
            return
        }

        if (loading.contains(t)){
            cuteLog("loading----$t")
            return
        }
        if (resultAd.containsKey(t)){
            val resultAdBean = resultAd[t]
            if (null!=resultAdBean?.ad){
                if (resultAdBean.notUse()){
                    deleteAdBean(t)
                }else{
                    cuteLog("has cache----$t")
                    return
                }
            }
        }

        val adList = getAdList(t)
        if(adList.isEmpty()){
            cuteLog("ad data empty----$t")
            return
        }
        loop(t,adList.iterator(),loadAgainOpen)
    }

    private fun loop(t: String, iterator: Iterator<CuteAdBean>, loadAgainOpen: Boolean){
        val next = iterator.next()
        load(t,next){
            if(null==it){
                if (iterator.hasNext()){
                    loop(t,iterator,loadAgainOpen)
                }else{
                    loading.remove(t)
                    if (t==CuteConf.OPEN&&loadAgainOpen){
                        loadAd(t,loadAgainOpen = false)
                    }
                }
            }else{
                loading.remove(t)
                resultAd[t]=it
            }
        }
    }

    private fun load(t:String,cuteAdBean: CuteAdBean,result:(bean:ResultAdBean?)->Unit){
        cuteLog("load as start--->${cuteAdBean}")
        when(cuteAdBean.type_cute){
            "kai"->{
                AppOpenAd.load(
                    cuteApp,
                    cuteAdBean.id_cute,
                    AdRequest.Builder().build(),
                    AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
                    object : AppOpenAd.AppOpenAdLoadCallback(){
                        override fun onAdLoaded(p0: AppOpenAd) {
                            super.onAdLoaded(p0)
                            cuteLog("success load,$t")
                            result.invoke(ResultAdBean(time = System.currentTimeMillis(), ad = p0))
                        }

                        override fun onAdFailedToLoad(p0: LoadAdError) {
                            super.onAdFailedToLoad(p0)
                            cuteLog("fail load,$t-->${p0.message}")
                            result.invoke(null)
                        }
                    }
                )
            }
            "cha"->{
                InterstitialAd.load(
                    cuteApp,
                    cuteAdBean.id_cute,
                    AdRequest.Builder().build(),
                    object : InterstitialAdLoadCallback(){

                        override fun onAdLoaded(p0: InterstitialAd) {
                            cuteLog("success load,$t")
                            result.invoke(ResultAdBean(time = System.currentTimeMillis(), ad = p0))
                        }

                        override fun onAdFailedToLoad(p0: LoadAdError) {
                            cuteLog("fail load,$t-->${p0.message}")
                            result.invoke(null)
                        }
                    }
                )
            }
            "yuan"->{
                AdLoader.Builder(
                    cuteApp,
                    cuteAdBean.id_cute,
                ).forNativeAd {
                    cuteLog("success load,$t")
                    result.invoke(ResultAdBean(time = System.currentTimeMillis(), ad = it))
                }
                    .withAdListener(object : AdListener(){
                        override fun onAdFailedToLoad(p0: LoadAdError) {
                            super.onAdFailedToLoad(p0)
                            cuteLog("fail load,$t-->${p0.message}--->${p0.code}")
                            result.invoke(null)
                        }

                        override fun onAdClicked() {
                            super.onAdClicked()
                            CuteAdNum.saveClick()
                        }
                    })
                    .withNativeAdOptions(
                        NativeAdOptions.Builder()
                            .setAdChoicesPlacement(
                                NativeAdOptions.ADCHOICES_BOTTOM_LEFT
                            )
                            .build()
                    )
                    .build()
                    .loadAd(AdRequest.Builder().build())

            }
        }
    }

    fun getAdBean(t: String)= resultAd[t]?.ad

    fun deleteAdBean(t: String){
        resultAd.remove(t)
    }

    fun removeAll(){
        resultAd.clear()
        loading.clear()
        loadAd(CuteConf.OPEN)
        loadAd(CuteConf.BOOK_MARK)
        loadAd(CuteConf.HISTORY)
        loadAd(CuteConf.TAB)
        loadAd(CuteConf.VPN_HOME)
        loadAd(CuteConf.VPN_CONNECT)
        loadAd(CuteConf.VPN_RESULT)
    }
}