package com.demo.cutebrowser.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.cutebrowser.R
import com.demo.cutebrowser.bean.VpnBean
import com.demo.cutebrowser.util.getVpnLogo
import com.demo.cutebrowser.util.show
import com.demo.cutebrowser.vpn.ConnectVpnManager
import com.demo.cutebrowser.vpn.VpnManager
import kotlinx.android.synthetic.main.item_vpn_list.view.*

class VpnListAdapter(
    private val context: Context,
    private val click:(bean:VpnBean)->Unit
):RecyclerView.Adapter<VpnListAdapter.VpnView>() {
    private val list= arrayListOf<VpnBean>()
    init {
        list.add(VpnBean())
        list.addAll(VpnManager.getVpnList())
    }

    inner class VpnView(view:View):RecyclerView.ViewHolder(view){
        init {
            view.setOnClickListener { click.invoke(list[layoutPosition]) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VpnView {
        return VpnView(LayoutInflater.from(context).inflate(R.layout.item_vpn_list,parent,false))
    }

    override fun onBindViewHolder(holder: VpnView, position: Int) {
        with(holder.itemView){
            val vpnBean = list[position]
            tv_vpn_name.text=vpnBean.csb_ccount
            iv_vpn_logo.setImageResource(getVpnLogo(vpnBean.csb_ccount))
            val b = vpnBean.csb_ip == ConnectVpnManager.server.csb_ip
            iv_sel.show(b)
            item_layout.isSelected=b
        }
    }

    override fun getItemCount(): Int = list.size
}