package com.cutesearching.privatebrowser.base

import android.os.Bundle
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import com.cutesearching.privatebrowser.R
import com.gyf.immersionbar.ImmersionBar

abstract class BaseActivity:AppCompatActivity() {
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
}