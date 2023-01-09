package com.cutesearching.privatebrowser.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.cutesearching.privatebrowser.R
import com.cutesearching.privatebrowser.manager.BrowserManager
import com.cutesearching.privatebrowser.util.showInvisible
import kotlinx.android.synthetic.main.item_tab.view.*

class TabListAdapter(
    private val ctx: Context,
    private val clickItem:(index:Int)->Unit,
    private val deleteItem:(index:Int)->Unit,
):RecyclerView.Adapter<TabListAdapter.MyView>() {
    inner class MyView(view:View):RecyclerView.ViewHolder(view){
        private val delete=view.findViewById<AppCompatImageView>(R.id.iv_delete)
        init {
            view.setOnClickListener {
                clickItem.invoke(layoutPosition)
            }
            delete.setOnClickListener {
                deleteItem.invoke(layoutPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyView {
        return MyView(LayoutInflater.from(ctx).inflate(R.layout.item_tab,parent,false))
    }

    override fun onBindViewHolder(holder: MyView, position: Int) {
        with(holder.itemView){
            val tabBean = BrowserManager.tabList[position]
            iv_web_url.text=tabBean.url
            if(tabBean.showHome){
                iv_web_logo.showInvisible(false)
                iv_web_url.showInvisible(false)
                if(null!=tabBean.homeBitmap){
                    iv_cover.scaleType= ImageView.ScaleType.FIT_CENTER
                    iv_cover.setImageBitmap(tabBean.homeBitmap)
                }
            }else{
                if(null!=tabBean.webBitmap){
                    iv_cover.scaleType= ImageView.ScaleType.CENTER_CROP
                    iv_cover.setImageBitmap(tabBean.webBitmap)
                }
            }
            if(tabBean.logo.isNotEmpty()){
                Glide.with(ctx)
                    .load(tabBean.logo)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(2)))
                    .into(iv_web_logo)
            }
        }
    }

    override fun getItemCount(): Int = BrowserManager.tabList.size

}