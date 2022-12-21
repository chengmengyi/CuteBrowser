package com.demo.cutebrowser.ac.browser

import androidx.recyclerview.widget.GridLayoutManager
import com.demo.cutebrowser.R
import com.demo.cutebrowser.admob.ShowFullAd
import com.demo.cutebrowser.adapter.TabListAdapter
import com.demo.cutebrowser.admob.CuteAdNum
import com.demo.cutebrowser.admob.LoadAd
import com.demo.cutebrowser.base.BaseActivity
import com.demo.cutebrowser.conf.CuteConf
import com.demo.cutebrowser.eventbus.EventBean
import com.demo.cutebrowser.eventbus.EventCode
import com.demo.cutebrowser.manager.BrowserManager
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