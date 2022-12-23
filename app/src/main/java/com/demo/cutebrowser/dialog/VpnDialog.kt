package com.demo.cutebrowser.dialog

import com.demo.cutebrowser.R
import com.demo.cutebrowser.base.BaseDialog
import com.demo.cutebrowser.util.PointManager
import kotlinx.android.synthetic.main.dialog_vpn.*

class VpnDialog(private val click:(sure:Boolean)->Unit):BaseDialog() {

    override fun layoutRes(): Int = R.layout.dialog_vpn

    override fun initView() {
        dialog?.setCancelable(false)
        PointManager.point("cute_vpnpo")
        tv_sure.setOnClickListener {
            PointManager.point("cute_vpnpo_con")
            dismiss()
            click.invoke(true)
        }
        iv_cancel.setOnClickListener {
            PointManager.point("cute_vpnpo_close")
            dismiss()
            click.invoke(false)
        }
    }
}