package com.cutesearching.privatebrowser.view

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cutesearching.privatebrowser.R
import com.cutesearching.privatebrowser.ac.browser.BookmarkActivity
import com.cutesearching.privatebrowser.ac.browser.HistoryActivity
import com.cutesearching.privatebrowser.adapter.FuncAdapter
import com.cutesearching.privatebrowser.eventbus.EventBean
import com.cutesearching.privatebrowser.eventbus.EventCode
import com.cutesearching.privatebrowser.manager.BrowserManager

class BrowserHomeView @JvmOverloads constructor(
    private val ctx: Context,
    attrs: AttributeSet? = null,
): LinearLayout(ctx, attrs) {
    private lateinit var rootView: ConstraintLayout

    init {
        val view = LayoutInflater.from(ctx).inflate(R.layout.layout_browser_home, this,false)
        val rvFunc=view.findViewById<RecyclerView>(R.id.rv_func)
        rootView=view.findViewById<ConstraintLayout>(R.id.root_view)
        rvFunc.apply {
            layoutManager=GridLayoutManager(ctx,4)
            adapter=FuncAdapter(ctx){
                when(it){
                    "Bookmark"->ctx.startActivity(Intent(ctx,BookmarkActivity::class.java))
                    "History"->ctx.startActivity(Intent(ctx,HistoryActivity::class.java))
                    else->BrowserManager.showWebView(ctx,it)
                }
            }
        }
        view.findViewById<View>(R.id.view_search).setOnClickListener {
            EventBean(EventCode.OPEN_SEARCH_DIALOG).send()
        }
        addView(view)
    }

    fun getRootLayoutBitmap(): Bitmap? {
        rootView.isDrawingCacheEnabled = true
        rootView.buildDrawingCache()
        return rootView.drawingCache
    }

}
