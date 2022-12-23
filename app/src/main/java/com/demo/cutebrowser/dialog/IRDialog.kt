package com.demo.cutebrowser.dialog

import com.demo.cutebrowser.R
import com.demo.cutebrowser.base.BaseDialog
import kotlinx.android.synthetic.main.dialog_ir.*

class IRDialog():BaseDialog() {

    override fun layoutRes(): Int = R.layout.dialog_ir

    override fun initView() {
        dialog?.setCancelable(false)
        tv_sure.setOnClickListener {
            dismiss()
        }
    }
}