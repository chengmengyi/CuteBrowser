package com.demo.cutebrowser.manager

import android.content.Context
import android.net.Uri
import android.view.View
import com.demo.cutebrowser.bean.TabBean
import com.demo.cutebrowser.interfaces.IBrowserCallback
import com.demo.cutebrowser.util.showToast
import com.demo.cutebrowser.view.BrowserHomeView
import com.demo.cutebrowser.view.BrowserWebView

object BrowserManager {
    private var tabIndex=0
    val tabList= arrayListOf<TabBean>()
    private var iBrowserCallback:IBrowserCallback?=null

    fun setCallback(iBrowserCallback: IBrowserCallback){
        this.iBrowserCallback=iBrowserCallback
    }

    fun showWebView(context: Context, url:String){
        val webView = initWebView(context)
        if(url.isNotEmpty()){
            webView.loadUrl(url)
        }
        tabList[tabIndex].showHome=false
        iBrowserCallback?.changeShowView(webView)
    }

    fun openNewWebView(context: Context,url: String){
        val browserHomeView = BrowserHomeView(context)
        val browserWebView = BrowserWebView(context, false)
        tabList.add(0,TabBean(homeView = browserHomeView, webView = browserWebView))
        tabIndex=0
        showWebView(context,url)
        iBrowserCallback?.updateTabSize()
    }

    private fun initWebView(context: Context):BrowserWebView{
        val webView = getWebView()
        return if(webView==null){
            if(tabList.isEmpty()){
                initHomeView(context)
            }
            val browserWebView = BrowserWebView(context, false)
            try {
                val tabBean = tabList[tabIndex]
                tabBean.webView=browserWebView
            }catch (e:Exception){

            }
            browserWebView
        }else{
            webView
        }
    }

    fun getWebView():BrowserWebView?{
        return try {
            tabList[tabIndex].webView
        }catch (e:Exception){
            null
        }
    }

    fun showHomeView(context: Context){
        val initHomeView = initHomeView(context)
        tabList[tabIndex].showHome=true
        iBrowserCallback?.changeShowView(initHomeView)
    }

    private fun initHomeView(context: Context):BrowserHomeView{
        val homeView = getHomeView()
        return if (homeView==null){
            val browserHomeView = BrowserHomeView(context)
            try {
                val tabBean = tabList[tabIndex]
                tabBean.homeView=browserHomeView
            }catch (e:Exception){
                tabList.add(TabBean(homeView=browserHomeView))
            }
            browserHomeView
        }else{
            homeView
        }
    }

    private fun getHomeView():BrowserHomeView?{
        return try {
            tabList[tabIndex].homeView
        }catch (e:Exception){
            null
        }
    }

    fun getCurrentShowHome():Boolean?{
        return try {
            tabList[tabIndex].showHome
        }catch (e:Exception){
            null
        }
    }

    fun setWebLogoTitle(url:String){
        try {
            val tabBean = tabList[tabIndex]
            tabBean.url=url
            val parse = Uri.parse(url)
            val logo="${parse.scheme}://${parse.host}/favicon.ico"
            tabBean.logo=logo
        }catch (e:Exception){

        }
    }

    fun showViewByIndex(index:Int,checkIndex:Boolean=true){
        try {
            if (index== tabIndex&&checkIndex){
                return
            }
            val tabBean = tabList[index]
            if(tabBean.showHome&&null!=tabBean.homeView){
                tabIndex=index
                iBrowserCallback?.changeShowView(tabBean.homeView!!)
            }
            if(!tabBean.showHome&&null!=tabBean.webView){
                tabIndex=index
                iBrowserCallback?.changeShowView(tabBean.webView!!)
            }
        }catch (e:Exception){

        }
    }

    fun addTab(context: Context){
        if(tabList.size>=10){
            context.showToast("Up to ten more")
            return
        }
        val browserHomeView = BrowserHomeView(context)
        tabList.add(0,TabBean(homeView = browserHomeView))
        tabIndex=0
        iBrowserCallback?.changeShowView(browserHomeView)
        iBrowserCallback?.updateTabSize()
    }

    fun deleteTab(index:Int){
        if(index >= tabList.size || tabList.isEmpty()){
            return
        }
        tabList.removeAt(index)
        if(index < tabIndex){
            tabIndex--
        }
        if (index == tabIndex){
            if(tabList.isEmpty()){

            }else{
                showViewByIndex(0,checkIndex = false)
            }
        }
        iBrowserCallback?.updateTabSize()
    }
}