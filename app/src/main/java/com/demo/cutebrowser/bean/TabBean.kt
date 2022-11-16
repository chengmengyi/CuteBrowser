package com.demo.cutebrowser.bean

import android.graphics.Bitmap
import com.demo.cutebrowser.view.BrowserHomeView
import com.demo.cutebrowser.view.BrowserWebView

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