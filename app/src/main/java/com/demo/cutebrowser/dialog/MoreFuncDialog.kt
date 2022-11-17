package com.demo.cutebrowser.dialog

import android.view.Gravity
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.cutebrowser.R
import com.demo.cutebrowser.adapter.MoreFuncAdapter
import com.demo.cutebrowser.base.BaseDialog
import kotlinx.android.synthetic.main.dialog_more_func.*

class MoreFuncDialog (private val clickItem:(content:String)->Unit):BaseDialog(){


    private val funcAdapter by lazy {  MoreFuncAdapter(requireContext()){
        clickItem.invoke(it)
        dismiss()
    }}

    override fun onStart() {
        super.onStart()
        mWindow?.setGravity(Gravity.BOTTOM)
        mWindow?.setWindowAnimations(R.style.BottomAnimation)
    }

    override fun layoutRes(): Int = R.layout.dialog_more_func

    override fun initView() {
        other_view.setOnClickListener { dismiss() }

        rv_more_func.apply {
            layoutManager=LinearLayoutManager(requireContext())
            adapter=funcAdapter
        }
    }
}