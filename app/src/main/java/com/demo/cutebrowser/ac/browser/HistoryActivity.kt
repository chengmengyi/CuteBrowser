package com.demo.cutebrowser.ac.browser

import android.content.Context
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.cutebrowser.R
import com.demo.cutebrowser.admob.ShowNativeAd
import com.demo.cutebrowser.adapter.HistoryAdapter
import com.demo.cutebrowser.base.BaseActivity
import com.demo.cutebrowser.bean.HistoryBean
import com.demo.cutebrowser.conf.CuteConf
import com.demo.cutebrowser.eventbus.EventBean
import com.demo.cutebrowser.eventbus.EventCode
import com.demo.cutebrowser.util.StorageUtil
import com.demo.cutebrowser.util.formatTime
import com.demo.cutebrowser.view.sticky.StickyItemDecoration
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.activity_bookmark.*
import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.android.synthetic.main.activity_history.edit_search
import kotlinx.android.synthetic.main.activity_history.iv_back
import kotlinx.android.synthetic.main.activity_history.refresh_layout

class HistoryActivity:BaseActivity(), OnRefreshLoadMoreListener{
    private var offset=0
    private var searchContent=""
    private val historyList= arrayListOf<HistoryBean>()
    private val show by lazy { ShowNativeAd(CuteConf.HISTORY,this) }

    private val historyAdapter by lazy { HistoryAdapter(this,historyList){
        EventBean(EventCode.SHOW_RECORD_URL, str = it.web).send()
        finish()
    } }

    override fun layoutRes(): Int = R.layout.activity_history

    override fun initView() {
        rv_history.apply {
            layoutManager=LinearLayoutManager(this@HistoryActivity)
            addItemDecoration(StickyItemDecoration())
            adapter=historyAdapter
        }
        setListener()
    }

    private fun setListener(){
        refresh_layout.setOnRefreshLoadMoreListener(this)
        refresh_layout.autoRefresh()
        iv_back.setOnClickListener { finish() }
        edit_search.setOnEditorActionListener(object : TextView.OnEditorActionListener{
            override fun onEditorAction(textView: TextView?, actionId: Int, keyEvent: KeyEvent?): Boolean {
                if ((actionId == EditorInfo.IME_ACTION_UNSPECIFIED || actionId == EditorInfo.IME_ACTION_SEARCH) && keyEvent != null) {
                    offset=0
                    searchContent = edit_search.text.toString().trim()
                    hideSoftKeyBoard()
                    refresh_layout.autoRefresh()
                    return true
                }
                return false
            }
        })
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        queryList()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        queryList()
    }

    private fun queryList() {
        val findAll = if (searchContent.isNullOrEmpty()){
            StorageUtil.queryHistory(offset)
        }else{
            StorageUtil.searchHistory(offset,searchContent)
        }

        if (!findAll.isNullOrEmpty()){
            if (offset==0){
                historyList.clear()
            }
            offset+=findAll.size
            if (historyList.isEmpty()){
                val first = findAll.first()
                historyList.add(HistoryBean(title = formatTime(first.time), itemType = 11, time = first.time))
            }
            findAll.forEach {
                val last = historyList.last()
                val lastTime=formatTime(last.time)
                val thisTime=formatTime(it.time)
                if (lastTime == thisTime){
                    historyList.add(it)
                }else{
                    historyList.add(HistoryBean(title = thisTime, itemType = 11, time = it.time))
                    historyList.add(it)
                }
            }
            historyAdapter.notifyDataSetChanged()
        }
        if (refresh_layout.isRefreshing){
            refresh_layout.finishRefresh()
        }
        if (refresh_layout.isLoading){
            refresh_layout.finishLoadMore()
        }
    }

    private fun hideSoftKeyBoard() {
        if (edit_search != null) {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(edit_search.getWindowToken(), 0);
        }
    }

    override fun onResume() {
        super.onResume()
        show.showAd()
    }

    override fun onDestroy() {
        super.onDestroy()
        show.stopShow()
    }
}