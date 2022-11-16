package com.demo.cutebrowser.dialog

import android.content.Context
import android.os.Handler
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.demo.cutebrowser.R
import com.demo.cutebrowser.base.BaseDialog
import com.demo.cutebrowser.eventbus.EventBean
import com.demo.cutebrowser.eventbus.EventCode
import com.demo.cutebrowser.util.showToast
import kotlinx.android.synthetic.main.dialog_search.*

class SearchDialog:BaseDialog() {

    override fun layoutRes(): Int = R.layout.dialog_search

    override fun initView() {
        iv_cancel.setOnClickListener { edit_search.setText("") }
        iv_search.setOnClickListener {
            search()
        }
        bottom_view.setOnClickListener { dismiss() }

        edit_search.setOnEditorActionListener(object : TextView.OnEditorActionListener{
            override fun onEditorAction(textView: TextView?, actionId: Int, keyEvent: KeyEvent?): Boolean {
                if ((actionId == EditorInfo.IME_ACTION_UNSPECIFIED || actionId == EditorInfo.IME_ACTION_SEARCH) && keyEvent != null) {
                    search()
                    return true
                }
                return false
            }
        })

        Handler().postDelayed({
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
            edit_search.requestFocus()
        },100)
    }

    private fun search(){
        val content = edit_search.text.toString().trim()
        if(content.isEmpty()){
            requireContext().showToast("Please enter the content")
            return
        }
        EventBean(EventCode.SEARCH_CONTENT, str = content).send()
        dismiss()
    }
}