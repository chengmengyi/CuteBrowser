package com.demo.cutebrowser.base

import android.os.Bundle
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import com.demo.cutebrowser.R
import com.gyf.immersionbar.ImmersionBar

abstract class BaseActivity:AppCompatActivity() {
    var onResume=false
    protected lateinit var immersionBar: ImmersionBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        height()
        setContentView(layoutRes())
        immersionBar= ImmersionBar.with(this).apply {
            statusBarColor(R.color.color_1f1f1f)
            fitsSystemWindows(true)
            statusBarDarkFont(false)
            init()
        }
        initView()
    }

    abstract fun layoutRes():Int

    abstract fun initView()

    private fun height(){
        val metrics: DisplayMetrics = resources.displayMetrics
        val td = metrics.heightPixels / 760f
        val dpi = (160 * td).toInt()
        metrics.density = td
        metrics.scaledDensity = td
        metrics.densityDpi = dpi
    }

    override fun onResume() {
        super.onResume()
        onResume=true
    }

    override fun onPause() {
        super.onPause()
        onResume=false
    }

    override fun onStop() {
        super.onStop()
        onResume=false
    }
}