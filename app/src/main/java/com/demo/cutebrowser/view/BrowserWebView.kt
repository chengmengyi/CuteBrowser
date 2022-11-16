package com.demo.cutebrowser.view

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import com.demo.cutebrowser.R
import com.demo.cutebrowser.manager.BrowserManager
import com.demo.cutebrowser.manager.SearchEngineManager
import com.demo.cutebrowser.util.StorageUtil
import com.demo.cutebrowser.util.cuteLog
import com.demo.cutebrowser.util.show

class BrowserWebView @JvmOverloads constructor(
    private val ctx: Context,
    private val incognito:Boolean,
    attrs: AttributeSet? = null,
) : LinearLayout(ctx, attrs) {
    private lateinit var webView:WebView
    private lateinit var editTitle: AppCompatEditText
    private lateinit var webProgress: ProgressBar

    init {
        val view = LayoutInflater.from(ctx).inflate(R.layout.layout_browser_web, this)
        webView=view.findViewById(R.id.web_view)
        editTitle=view.findViewById(R.id.edit_title)
        webProgress=view.findViewById(R.id.web_progress)
        view.findViewById<AppCompatImageView>(R.id.iv_refresh).setOnClickListener { reLoadUrl() }
        view.findViewById<AppCompatImageView>(R.id.iv_home).setOnClickListener { BrowserManager.showHomeView(context) }

        editTitle.setOnEditorActionListener(object : TextView.OnEditorActionListener{
            override fun onEditorAction(textView: TextView?, actionId: Int, keyEvent: KeyEvent?): Boolean {
                if ((actionId == EditorInfo.IME_ACTION_UNSPECIFIED || actionId == EditorInfo.IME_ACTION_SEARCH) && keyEvent != null) {
                    loadUrl(SearchEngineManager.getSearchUrl(editTitle.text.toString()))
                    return true
                }
                return false
            }
        })

        initWebView()
    }

    fun loadUrl(url:String){
        webView.loadUrl(url)
    }

    fun reLoadUrl(){
        webProgress.show(true)
        webView.reload()
    }

    fun back():Boolean{
        return if (webView.canGoBack()){
            webView.goBack()
            true
        }else{
            false
        }
    }

    fun forward():Boolean{
        return if (webView.canGoForward()){
            webView.goForward()
            true
        }else{
            false
        }
    }

    fun getWebBitmap():Bitmap?{
        return try {
            webView.isDrawingCacheEnabled=true
            webView.drawingCache
        }catch (e: Exception){
            null
        }
    }

    fun addBookmark(){
        StorageUtil.saveBookmark(ctx,editTitle.text?.toString()?:"",webView.url?:"")
    }

    private fun initWebView(){
        webView.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                importantForAutofill = View.IMPORTANT_FOR_AUTOFILL_YES
            }
            isScrollbarFadingEnabled = true
            isSaveEnabled = true
            overScrollMode = View.OVER_SCROLL_NEVER
            setNetworkAvailable(true)
            with(webView.settings) {
                javaScriptEnabled = true
                mediaPlaybackRequiresUserGesture = true

                mixedContentMode = if (!incognito) {
                    WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
                } else {
                    WebSettings.MIXED_CONTENT_NEVER_ALLOW
                }

                if (!incognito) {
                    domStorageEnabled = true
                    setAppCacheEnabled(true)
                    databaseEnabled = true
                    cacheMode = WebSettings.LOAD_DEFAULT
                } else {
                    domStorageEnabled = false
                    setAppCacheEnabled(false)
                    databaseEnabled = false
                    cacheMode = WebSettings.LOAD_NO_CACHE
                }

                setSupportZoom(true)
                builtInZoomControls = true
                displayZoomControls = false
                allowContentAccess = true
                allowFileAccess = true
                allowFileAccessFromFileURLs = false
                allowUniversalAccessFromFileURLs = false
            }
            webViewClient=object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                    return handleUrl(view,url)
                }

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    BrowserManager.setWebLogoTitle(url?:"")
                    StorageUtil.saveHistory(view?.title?:"",url?:"")
//                    LitePalUtil.saveHistoryUrl(view?.title?:"",url?:"")
//                    if (firstLoad){
//                        firstLoad=false
//                        LitePalUtil.saveRecent(view?.title?:"",url?:"")
//                    }
//                    listener.updateBottomBtnState(updateBack = true, updateForward = webView.canGoForward(),updateMore = true)
                }
            }
            webChromeClient=object : WebChromeClient(){
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    super.onProgressChanged(view, newProgress)
                    webProgress.progress=newProgress
                    if (newProgress>=100){
                        webProgress.show(false)
                    }
                }

                override fun onReceivedTitle(view: WebView?, title: String?) {
                    super.onReceivedTitle(view, title)
                    editTitle.setText(title?:"")
                }

                override fun onReceivedIcon(view: WebView?, icon: Bitmap?) {
                    super.onReceivedIcon(view, icon)

                }
            }
        }
    }

    private fun handleUrl(view:WebView,url:String):Boolean{
        if(url.startsWith("http")){
            view.loadUrl(url)
            return true
        }
        val packageManager = ctx.packageManager
        val intent = Intent(Intent.ACTION_VIEW).setData(Uri.parse(url))
        if(intent.resolveActivity(packageManager)!=null){
            runCatching {
                ctx.startActivity(intent)
                return true
            }
        }
        if (url.startsWith("intent:")){
            try {
                val parseUri = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
                if(parseUri.resolveActivity(packageManager)!=null||parseUri.data?.scheme=="market"){
                    runCatching {
                        ctx.startActivity(parseUri)
                    }
                    return true
                }
                ctx.startActivity(parseUri)
                return true
            }catch (e:Exception){
                return false
            }
        }
        runCatching {
            ctx.startActivity(intent)
        }
        return true
    }
}