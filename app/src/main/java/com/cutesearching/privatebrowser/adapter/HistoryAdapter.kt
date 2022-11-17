package com.cutesearching.privatebrowser.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.cutesearching.privatebrowser.R
import com.cutesearching.privatebrowser.bean.HistoryBean
import kotlinx.android.synthetic.main.item_history_content.view.*
import kotlinx.android.synthetic.main.item_history_title.view.*

class HistoryAdapter(
    private val ctx:Context,
    private val list:ArrayList<HistoryBean>,
    private val clickItem:(bean:HistoryBean)->Unit
):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class TitleView(view:View):RecyclerView.ViewHolder(view){
        init {
            view.tag = true
        }
    }

    inner class ContentView(view:View):RecyclerView.ViewHolder(view){
        init {
            view.tag = false
            view.setOnClickListener {
                clickItem.invoke(list[layoutPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType==10){
            return ContentView(LayoutInflater.from(ctx).inflate(R.layout.item_history_content,parent,false))
        }
        return TitleView(LayoutInflater.from(ctx).inflate(R.layout.item_history_title,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val historyBean = list[position]
        with(holder.itemView){
            if(holder is TitleView){
                tv_title.text= historyBean.title
            }
            if (holder is ContentView){
                tv_title_content.text= historyBean.title
                tv_url.text=historyBean.web
                Glide.with(ctx)
                    .load(historyBean.icon)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(10)))
                    .into(iv_logo)
            }
        }
    }

    override fun getItemViewType(position: Int): Int = list[position].itemType

    override fun getItemCount(): Int = list.size
}