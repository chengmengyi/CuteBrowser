package com.demo.cutebrowser.ac

import android.animation.ValueAnimator
import android.content.Intent
import android.view.KeyEvent
import android.view.animation.LinearInterpolator
import androidx.core.animation.doOnEnd
import com.blankj.utilcode.util.ActivityUtils
import com.demo.cutebrowser.R
import com.demo.cutebrowser.ac.browser.BrowserActivity
import com.demo.cutebrowser.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(){
    private lateinit var progressAnimator:ValueAnimator

    override fun layoutRes(): Int = R.layout.activity_main

    override fun initView() {
        start()
    }

    private fun start(){
        progressAnimator=ValueAnimator.ofInt(0, 100).apply {
            duration = 3000L
            interpolator = LinearInterpolator()
            addUpdateListener {
                val progress = it.animatedValue as Int
                view_progress.progress = progress
            }
            doOnEnd {
                toBrowserAc()
            }
            start()
        }
    }

    private fun toBrowserAc(){
        val activityExistsInStack = ActivityUtils.isActivityExistsInStack(BrowserActivity::class.java)
        if (!activityExistsInStack){
            startActivity(Intent(this,BrowserActivity::class.java))
        }
        finish()
    }

    private fun stop(){
        progressAnimator.cancel()
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode== KeyEvent.KEYCODE_BACK){
            return true
        }
        return false
    }

    override fun onResume() {
        super.onResume()
        progressAnimator.resume()
    }

    override fun onPause() {
        super.onPause()
        progressAnimator.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        stop()
    }
}