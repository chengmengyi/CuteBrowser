package com.demo.cutebrowser.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.cutebrowser.R
import com.demo.cutebrowser.bean.FuncBean
import kotlinx.android.synthetic.main.item_func.view.*

class FuncAdapter(
    private val context: Context,
    private val click:(url:String)->Unit
):RecyclerView.Adapter<FuncAdapter.MyView>() {
    private val list= arrayListOf<FuncBean>()
    init {
        list.clear()
        list.add(FuncBean("Instagram",R.drawable.instagram, "https://www.instagram.com/"))
//        list.add(FuncBean("Instagram",R.drawable.instagram, "https://www.baidu.com/"))
        list.add(FuncBean("Netfilx",R.drawable.youtube, "https://www.netflix.com/"))
        list.add(FuncBean("Facebook",R.drawable.facebook, "https://www.facebook.com/"))
        list.add(FuncBean("Twitter",R.drawable.twitter, "https://www.twitter.com/"))
        list.add(FuncBean("Bookmark",R.drawable.book, "Bookmark"))
        list.add(FuncBean("History",R.drawable.history, "History"))
    }

    inner class MyView(view:View):RecyclerView.ViewHolder(view){
        init {
            view.setOnClickListener {
                click.invoke(list[layoutPosition].url)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyView {
        return MyView(LayoutInflater.from(context).inflate(R.layout.item_func,parent,false))
    }

    override fun onBindViewHolder(holder: MyView, position: Int) {
        with(holder.itemView){
            val funcBean = list[position]
            tv_title.text=funcBean.content
            iv_func.setImageResource(funcBean.logo)
        }
    }

    override fun getItemCount(): Int = list.size
}