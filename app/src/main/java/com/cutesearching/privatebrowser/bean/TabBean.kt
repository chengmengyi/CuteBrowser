package com.cutesearching.privatebrowser.bean

import android.graphics.Bitmap
import com.cutesearching.privatebrowser.view.BrowserHomeView
import com.cutesearching.privatebrowser.view.BrowserWebView

class TabBean(
    var logo:String="",
    var url:String="",
    var showHome:Boolean=true,
    var homeBitmap: Bitmap?=null,
    var webBitmap:Bitmap?=null,
    var homeView: BrowserHomeView?=null,
    var webView: BrowserWebView?=null
) {
}