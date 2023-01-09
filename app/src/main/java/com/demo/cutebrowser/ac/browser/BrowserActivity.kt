package com.demo.cutebrowser.ac.browser

import android.content.Intent
import android.view.KeyEvent
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.demo.cutebrowser.R
import com.demo.cutebrowser.adapter.BottomAdapter
import com.demo.cutebrowser.base.BaseActivity
import com.demo.cutebrowser.dialog.MoreFuncDialog
import com.demo.cutebrowser.dialog.SearchDialog
import com.demo.cutebrowser.eventbus.EventBean
import com.demo.cutebrowser.eventbus.EventCode
import com.demo.cutebrowser.interfaces.IBrowserCallback
import com.demo.cutebrowser.manager.BrowserManager
import com.demo.cutebrowser.manager.SearchEngineManager
import kotlinx.android.synthetic.main.activity_browser.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.lang.System.exit

import com.demo.cutebrowser.ac.vpn.VpnHomeActivity
import com.demo.cutebrowser.admob.ShowNativeAd
import com.demo.cutebrowser.conf.CuteConf
import com.demo.cutebrowser.conf.CuteFirebase
import com.demo.cutebrowser.dialog.IRDialog
import com.demo.cutebrowser.dialog.VpnDialog
import com.demo.cutebrowser.util.*
import com.demo.cutebrowser.view.BrowserHomeView
import com.demo.cutebrowser.vpn.ConnectVpnManager
import com.tencent.mmkv.MMKV
import kotlinx.android.synthetic.main.layout_home_bottom.*
import kotlinx.coroutines.*
import java.util.*

class BrowserActivity:BaseActivity(), IBrowserCallback {
    private var clickTab=false
    private var showingVpnDialog=false
    private val bottomAdapter by lazy { BottomAdapter(this@BrowserActivity){ clickBottom(it) } }
    private val showAd by lazy { ShowNativeAd(CuteConf.HOME,this) }

    override fun layoutRes(): Int = R.layout.activity_browser

    override fun initView() {
        BrowserManager.setCallback(this)
        BrowserManager.showHomeView(this)
        setAdapter()
        EventBus.getDefault().register(this)
        iv_vpn.setOnClickListener {
            if(CuteFirebase.isIR){
                IRDialog().show(supportFragmentManager,"IRDialog")
                return@setOnClickListener
            }
            doPlanB(false)
        }
    }

    override fun changeShowView(view: View) {
        layout_home_bottom.show(view is BrowserHomeView)
        frameLayout.removeAllViews()
        frameLayout.removeAllViewsInLayout()
        frameLayout.addView(view)
    }

    override fun updateTabSize() {
        bottomAdapter.updateTabSize()
    }

    private fun setAdapter(){
        rv_bottom.apply {
            layoutManager=GridLayoutManager(this@BrowserActivity,5)
            adapter=bottomAdapter
        }
    }

    private fun clickBottom(index:Int){
        when(index){
            0->clickBack()
            1->clickForward()
            2->clickShowHome()
            3->clickTab()
            4->showMoreFuncDialog()
        }
    }

    private fun clickBack(closeAc:Boolean=false){
        val currentShowHome = BrowserManager.getCurrentShowHome()
        if(currentShowHome==false){
            val webView = BrowserManager.getWebView() ?: return
            if (!webView.back()){
                BrowserManager.showHomeView(this)
            }
        }else{
            if(closeAc){
                onDestroy()
            }else{
                funcEnable()
            }
        }
    }

    private fun clickForward(){
        val browserWebView = BrowserManager.getWebView() ?: return
        when (BrowserManager.getCurrentShowHome()) {
            true -> {
                BrowserManager.showWebView(this,"")
            }
            false -> {
                browserWebView.forward()
            }
            else -> {
                funcEnable()
            }
        }
    }

    private fun clickShowHome(){
        val currentShowHome = BrowserManager.getCurrentShowHome()
        if(currentShowHome==false){
            BrowserManager.showHomeView(this)
        }else{
            funcEnable()
        }
    }

    private fun clickTab(){
        if(!clickTab){
            clickTab=true
            GlobalScope.launch {
                for (tabBean in BrowserManager.tabList) {
                    if(tabBean.showHome){
                        tabBean.homeBitmap=tabBean.homeView?.getRootLayoutBitmap()
                    }else{
                        tabBean.webBitmap=tabBean.webView?.getWebBitmap()
                    }
                }
                withContext(Dispatchers.Main){
                    startActivity(Intent(this@BrowserActivity,TabListActivity::class.java))
                    clickTab=false
                }
            }
        }
    }

    private fun showMoreFuncDialog(){
        if(BrowserManager.getCurrentShowHome()==false){
            val moreFuncDialog = MoreFuncDialog {
                clickMoreFuncItem(it)
            }
            moreFuncDialog.show(supportFragmentManager,"MoreFuncDialog")
        }else{
            funcEnable()
        }
    }

    private fun clickMoreFuncItem(content:String){
        when(content){
            "Reload"->{
                val webView = BrowserManager.getWebView()
                webView?.reLoadUrl()
            }
            "Add Bookmark"->{
                val webView = BrowserManager.getWebView()
                webView?.addBookmark()
            }
            "Bookmark"->startActivity(Intent(this,BookmarkActivity::class.java))
            "History"->startActivity(Intent(this,HistoryActivity::class.java))
        }
    }

    @Subscribe
    fun onEvent(bean: EventBean) {
        when(bean.code){
            //打开搜索弹窗
            EventCode.OPEN_SEARCH_DIALOG->{
                SearchDialog().show(supportFragmentManager,"SearchDialog")
            }
            //搜索内容
            EventCode.SEARCH_CONTENT->{
                BrowserManager.showWebView(this,SearchEngineManager.getSearchUrl(bean.str))
            }
            //增加标签页
            EventCode.ADD_TAB->{
                BrowserManager.addTab(this)
            }
            //显示书签/历史记录的url
            EventCode.SHOW_RECORD_URL->{
                if(bean.str.isNotEmpty()){
                    BrowserManager.openNewWebView(this,bean.str)
                }
            }
        }
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode== KeyEvent.KEYCODE_BACK){
            clickBack(closeAc = true)
            return true
        }
        return false
    }

    override fun onResume() {
        super.onResume()
        if (ReloadAdManager.reload(CuteConf.HOME)){
            showAd.showAd()
        }
        updateVpnInfo()
        checkPlan()
    }

    private fun updateVpnInfo(){
        if(ConnectVpnManager.connected()){
            val server = ConnectVpnManager.server
            tv_vpn_name.text=server.csb_ccount
            iv_vpn_logo.setImageResource(getVpnLogo(server.csb_ccount))
        }
    }

    private fun checkPlan(){
        GlobalScope.launch(Dispatchers.Main) {
            delay(300L)
            if(!onResume){
                return@launch
            }
            withContext(Dispatchers.Main){
                if (ActivityCallback.loadAppType==2){
                    return@withContext
                }
                if(!isBuyUser(getReferrerStr())){
                    doPlanA()
                    PointManager.setUser("a")
                    return@withContext
                }
                if(CuteFirebase.planType.isEmpty()){
                    val nextInt = Random().nextInt(100)
                    CuteFirebase.planType = if (CuteFirebase.csb_test.isEmpty()){
                        if (80>=nextInt) "B" else "A"
                    }else{
                        if (str2Int(CuteFirebase.csb_test)>=nextInt) "B" else "A"
                    }
                }
                PointManager.setUser(CuteFirebase.planType.toLowerCase())
                if(CuteFirebase.planType=="B"&&ConnectVpnManager.disconnected()){
                    doPlanB(true)
                }else{
                    doPlanA()
                }
            }
        }
    }

    private fun doPlanA(){
        if (CuteFirebase.csb_shpop=="2"){
            if (ActivityCallback.loadAppType==0){
                checkReffer()
            }
            ActivityCallback.loadAppType=2
        }else if (CuteFirebase.csb_shpop=="1"){
            if (ActivityCallback.loadAppType!=2){
                checkReffer()
            }
            ActivityCallback.loadAppType=2
        }
    }

    private fun checkReffer(){
        when(CuteFirebase.csb_reffer){
            "1"->showVpnDialog()
            "2"->{
                if (isBuyUser(getReferrerStr())){
                    showVpnDialog()
                }
            }
            "3"->{
                val referrerStr = getReferrerStr()
                if(referrerStr.contains("facebook")||referrerStr.contains("fb4a")){
                    showVpnDialog()
                }
            }
        }
    }

    private fun showVpnDialog(){
        if(ConnectVpnManager.disconnected()&&!showingVpnDialog){
            showingVpnDialog=true
            VpnDialog{
                showingVpnDialog=false
                if (it){
                    doPlanB(true)
                }
            }.show(supportFragmentManager,"VpnDialog")
        }
    }

    private fun doPlanB(autoConnect:Boolean){
        ActivityCallback.loadAppType=2
        if(CuteFirebase.isIR){
            IRDialog().show(supportFragmentManager,"IRDialog")
            return
        }
        startActivity(Intent(this,VpnHomeActivity::class.java).apply {
            putExtra("autoConnect",autoConnect)
        })
    }

    private fun getReferrerStr() = MMKV.defaultMMKV().decodeString("referrer", "")?:""

    override fun onDestroy() {
        super.onDestroy()
        showAd.stopShow()
        ReloadAdManager.setBool(CuteConf.HOME,true)
        EventBus.getDefault().unregister(this)
        exit(0)
    }
}