package com.cutesearching.privatebrowser.ac.browser

import android.content.Intent
import android.view.KeyEvent
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.cutesearching.privatebrowser.R
import com.cutesearching.privatebrowser.adapter.BottomAdapter
import com.cutesearching.privatebrowser.base.BaseActivity
import com.cutesearching.privatebrowser.dialog.MoreFuncDialog
import com.cutesearching.privatebrowser.dialog.SearchDialog
import com.cutesearching.privatebrowser.eventbus.EventBean
import com.cutesearching.privatebrowser.eventbus.EventCode
import com.cutesearching.privatebrowser.interfaces.IBrowserCallback
import com.cutesearching.privatebrowser.manager.BrowserManager
import com.cutesearching.privatebrowser.manager.SearchEngineManager
import com.cutesearching.privatebrowser.util.ActivityCallback
import com.cutesearching.privatebrowser.util.funcEnable
import kotlinx.android.synthetic.main.activity_browser.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.lang.System.exit


class BrowserActivity:BaseActivity(), IBrowserCallback {
    private var clickTab=false
    private val bottomAdapter by lazy { BottomAdapter(this@BrowserActivity){ clickBottom(it) } }

    override fun layoutRes(): Int = R.layout.activity_browser

    override fun initView() {
        BrowserManager.setCallback(this)
        BrowserManager.showHomeView(this)
        setAdapter()
        EventBus.getDefault().register(this)
    }

    override fun changeShowView(view: View) {
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


    override fun onDestroy() {
        super.onDestroy()
        ActivityCallback.refreshNativeAd=true
        EventBus.getDefault().unregister(this)
        exit(0)
    }
}