package com.demo.cutebrowser.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.demo.cutebrowser.R
import com.gyf.immersionbar.ImmersionBar

abstract class BaseDialog: DialogFragment() {
    protected var mWindow: Window?=null
    protected var immersionBar: ImmersionBar?=null

    override fun onStart() {
        super.onStart()
        mWindow=dialog?.window
        mWindow?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BaseDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutRes(),container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        immersionBar= ImmersionBar.with(this).apply {
//            statusBarColor(R.color.color_1f1f1f)
//            fitsSystemWindows(true)
//            statusBarDarkFont(false)
//            init()
//        }

        immersionBar=ImmersionBar.with(this).apply {
            statusBarAlpha(0f)
            autoDarkModeEnable(true)
            statusBarDarkFont(false)
            init()
        }
        initView()
    }

    abstract fun initView()

    abstract fun layoutRes():Int
}