package com.demo.cutebrowser.dialog

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.SizeUtils
import com.demo.cutebrowser.R
import com.demo.cutebrowser.adapter.MoreFuncAdapter

class MoreFuncDialog (
    private val context: Context,
    private val clickItem:(content:String)->Unit
){
    private var mPopupWindow: PopupWindow?=null
    private var moreFuncAdapter:MoreFuncAdapter?=null

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_more_func, null)
        mPopupWindow= PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        mPopupWindow?.isFocusable=true
        mPopupWindow?.isOutsideTouchable=true

        val rvMoreFunc = view.findViewById<RecyclerView>(R.id.rv_more_func)
        moreFuncAdapter= MoreFuncAdapter(context){
            clickItem.invoke(it)
            mPopupWindow?.dismiss()
        }
        rvMoreFunc.apply {
            layoutManager= LinearLayoutManager(context)
            adapter=moreFuncAdapter
        }
    }

    fun show(view: View){
        mPopupWindow?.showAtLocation(view, Gravity.BOTTOM,0, SizeUtils.dp2px(66F))
    }

}