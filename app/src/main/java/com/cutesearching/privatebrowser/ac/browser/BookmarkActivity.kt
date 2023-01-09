package com.cutesearching.privatebrowser.ac.browser

import android.content.Context
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.cutesearching.privatebrowser.R
import com.cutesearching.privatebrowser.ac.ShowFullAd
import com.cutesearching.privatebrowser.adapter.BookmarkAdapter
import com.cutesearching.privatebrowser.admob.LoadAd
import com.cutesearching.privatebrowser.base.BaseActivity
import com.cutesearching.privatebrowser.bean.BookmarkBean
import com.cutesearching.privatebrowser.conf.CuteConf
import com.cutesearching.privatebrowser.eventbus.EventBean
import com.cutesearching.privatebrowser.eventbus.EventCode
import com.cutesearching.privatebrowser.util.StorageUtil
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.activity_bookmark.*

class BookmarkActivity:BaseActivity(), OnRefreshLoadMoreListener {
    private var offset=0
    private var searchContent=""
    private val bookmarkList= arrayListOf<BookmarkBean>()
    private val show by lazy { ShowFullAd(CuteConf.BOOK_MARK,this){finish()} }

    private val bookAdapter by lazy { BookmarkAdapter(
        this,
        bookmarkList,
        clickItem = {
            EventBean(EventCode.SHOW_RECORD_URL, str = it.web).send()
            finish()
        }
    ) }

    override fun layoutRes(): Int = R.layout.activity_bookmark

    override fun initView() {
        LoadAd.loadAd(CuteConf.BOOK_MARK)
        rv_bookmark.apply {
            layoutManager=LinearLayoutManager(this@BookmarkActivity)
            adapter=bookAdapter
        }
        setListener()
    }

    private fun setListener(){
        refresh_layout.setOnRefreshLoadMoreListener(this)
        refresh_layout.autoRefresh()
        iv_back.setOnClickListener { onBackPressed() }
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

    private fun queryList(){
        val findAll = if (searchContent.isEmpty()){
            StorageUtil.queryBookmark(offset)
        }else{
            StorageUtil.searchBookmark(offset,searchContent)
        }

        if (offset==0){
            bookmarkList.clear()
        }
        offset+=findAll.size
        bookmarkList.addAll(findAll)
        bookAdapter.notifyDataSetChanged()

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

    override fun onBackPressed() {
        if(LoadAd.getAdBean(CuteConf.BOOK_MARK)!=null){
            show.showFullAd {
                if (it){
                    finish()
                }
            }
            return
        }
        finish()
    }
}