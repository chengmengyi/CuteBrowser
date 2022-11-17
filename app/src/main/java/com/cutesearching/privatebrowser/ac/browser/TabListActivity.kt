package com.cutesearching.privatebrowser.ac.browser

import androidx.recyclerview.widget.GridLayoutManager
import com.cutesearching.privatebrowser.R
import com.cutesearching.privatebrowser.adapter.TabListAdapter
import com.cutesearching.privatebrowser.base.BaseActivity
import com.cutesearching.privatebrowser.eventbus.EventBean
import com.cutesearching.privatebrowser.eventbus.EventCode
import com.cutesearching.privatebrowser.manager.BrowserManager
import kotlinx.android.synthetic.main.activity_tab_list.*

class TabListActivity:BaseActivity() {
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
            view_add.performClick()
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