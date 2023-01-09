package com.cutesearching.privatebrowser.ac.browser

import androidx.recyclerview.widget.GridLayoutManager
import com.cutesearching.privatebrowser.R
import com.cutesearching.privatebrowser.ac.ShowFullAd
import com.cutesearching.privatebrowser.adapter.TabListAdapter
import com.cutesearching.privatebrowser.admob.CuteAdNum
import com.cutesearching.privatebrowser.admob.LoadAd
import com.cutesearching.privatebrowser.base.BaseActivity
import com.cutesearching.privatebrowser.conf.CuteConf
import com.cutesearching.privatebrowser.eventbus.EventBean
import com.cutesearching.privatebrowser.eventbus.EventCode
import com.cutesearching.privatebrowser.manager.BrowserManager
import kotlinx.android.synthetic.main.activity_tab_list.*

class TabListActivity:BaseActivity() {
    private val show by lazy { ShowFullAd(CuteConf.TAB,this){finish()} }
    private val tabAdapter by lazy { TabListAdapter(
        this,
        clickItem = {
            clickItem(it)
        },
        deleteItem = {
            deleteItem(it)
        }
    )}

    override fun layoutRes(): Int = R.layout.activity_tab_list

    override fun initView() {
        setAdapter()
        iv_back.setOnClickListener { finish() }
        view_add.setOnClickListener {
            addTab()
        }
    }

    private fun addTab(){
        CuteAdNum.addTabNum++
        if(CuteAdNum.checkShowTabAd()&&null!=LoadAd.getAdBean(CuteConf.TAB)){
            show.showFullAd {
                if (it){
                    EventBean(EventCode.ADD_TAB).send()
                    finish()
                }
            }
        }else{
            EventBean(EventCode.ADD_TAB).send()
            finish()
        }
    }

    private fun clickItem(index:Int){
        BrowserManager.showViewByIndex(index)
        finish()
    }

    private fun deleteItem(index:Int){
        BrowserManager.deleteTab(index)
        if(BrowserManager.tabList.isEmpty()){
            EventBean(EventCode.ADD_TAB).send()
            finish()
        }else{
            tabAdapter.notifyDataSetChanged()
        }
    }

    private fun setAdapter(){
        rv_tab.apply {
            layoutManager=GridLayoutManager(this@TabListActivity,2)
            adapter=tabAdapter
        }
    }
}