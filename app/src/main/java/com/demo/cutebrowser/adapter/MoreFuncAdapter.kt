package com.demo.cutebrowser.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.cutebrowser.R
import com.demo.cutebrowser.bean.FuncBean
import kotlinx.android.synthetic.main.item_more_func.view.*

class MoreFuncAdapter(
    private val context: Context,
    private val clickItem:(content:String)->Unit
):RecyclerView.Adapter<MoreFuncAdapter.MyView>() {
    private val list= arrayListOf<FuncBean>()
    init {
        list.clear()
        list.add(FuncBean("Reload",R.drawable.icon_reload,""))
        list.add(FuncBean("Add Bookmark",R.drawable.icon_add_book,""))
        list.add(FuncBean("Bookmark",R.drawable.icon_book,""))
        list.add(FuncBean("History",R.drawable.icon_history,""))
    }

    inner class MyView(view:View):RecyclerView.ViewHolder(view){
        init {
            view.setOnClickListener { clickItem.invoke(list[layoutPosition].content) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyView {
        return MyView(LayoutInflater.from(context).inflate(R.layout.item_more_func,parent,false))
    }

    override fun onBindViewHolder(holder: MyView, position: Int) {
        with(holder.itemView){
            val funcBean = list[position]
            tv_title.text=funcBean.content
            iv_icon.setImageResource(funcBean.logo)
        }
    }

    override fun getItemCount(): Int = list.size

}