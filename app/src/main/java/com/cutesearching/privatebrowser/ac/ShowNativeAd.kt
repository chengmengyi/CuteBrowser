package com.cutesearching.privatebrowser.ac

import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.utils.widget.ImageFilterView
import com.cutesearching.privatebrowser.R
import com.cutesearching.privatebrowser.admob.CuteAdNum
import com.cutesearching.privatebrowser.admob.LoadAd
import com.cutesearching.privatebrowser.base.BaseActivity
import com.cutesearching.privatebrowser.util.ActivityCallback
import com.cutesearching.privatebrowser.util.cuteLog
import com.cutesearching.privatebrowser.util.show
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import kotlinx.coroutines.*

class ShowNativeAd(
    private val t:String,
    private val baseActivity: BaseActivity
) {
    private var lastAd:NativeAd?=null
    private var showJob:Job?=null
    
    fun showAd(){
        LoadAd.loadAd(t)
        stopShow()
        showJob= GlobalScope.launch(Dispatchers.Main) {
            delay(300L)
            if (!baseActivity.onResume){
                return@launch
            }
            while (true) {
                if (!isActive) {
                    break
                }
                val adBean = LoadAd.getAdBean(t)
                if (baseActivity.onResume&&null != adBean&&adBean is NativeAd) {
                    cancel()
                    lastAd?.destroy()
                    lastAd=adBean
                    showAd(adBean)
                }
                delay(1000L)
            }
        }
    }
    
    private fun showAd(ad: NativeAd){
        cuteLog("start show $t ad ")
        val adView = baseActivity.findViewById<NativeAdView>(R.id.ad_view)
        adView.iconView=baseActivity.findViewById(R.id.ad_logo)
        (adView.iconView as ImageFilterView).setImageDrawable(ad.icon?.drawable)

        adView.callToActionView=baseActivity.findViewById(R.id.ad_btn)
        (adView.callToActionView as AppCompatTextView).text=ad.callToAction

//        if(type!=AdType.APP_LOCK_HOME_AD){
//            adView.mediaView=baseActivity.findViewById(R.id.native_cover)
//            adRes.mediaContent?.let {
//                adView.mediaView?.apply {
//                    setMediaContent(it)
//                    setImageScaleType(ImageView.ScaleType.CENTER_CROP)
//                    outlineProvider = appLockProvider
//                }
//            }
//        }

        adView.bodyView=baseActivity.findViewById(R.id.ad_desc)
        (adView.bodyView as AppCompatTextView).text=ad.body

        adView.headlineView=baseActivity.findViewById(R.id.ad_title)
        (adView.headlineView as AppCompatTextView).text=ad.headline

        adView.setNativeAd(ad)
        baseActivity.findViewById<AppCompatImageView>(R.id.ad_cover).show(false)
        CuteAdNum.saveShow()
        LoadAd.deleteAdBean(t)
        LoadAd.loadAd(t)
        ActivityCallback.refreshNativeAd=false
    }
    
    fun stopShow(){
        showJob?.cancel()
        showJob=null
    }
}