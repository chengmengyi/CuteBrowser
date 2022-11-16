package com.demo.cutebrowser.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.demo.cutebrowser.R
import com.demo.cutebrowser.bean.BookmarkBean
import com.demo.cutebrowser.util.StorageUtil
import com.demo.cutebrowser.view.slidelayout.SlideHelper
import com.demo.cutebrowser.view.slidelayout.SlideLayout
import kotlinx.android.synthetic.main.item_bookmark.view.*

class BookmarkAdapter(
    private val ctx: Context,
    private val list:ArrayList<BookmarkBean>,
    private val clickItem:(bookmarkBean:BookmarkBean)->Unit,
):RecyclerView.Adapter<BookmarkAdapter.MyView>() {
    private val slideHelper = SlideHelper()

    inner class MyView(view:View):RecyclerView.ViewHolder(view){
        private val slideLayout=view.findViewById<SlideLayout>(R.id.slide_layout)
        private val tvDelete=view.findViewById<AppCompatTextView>(R.id.tv_delete)
        init {
            view.setOnClickListener {
                if (slideLayout.isOpen){
                    slideLayout.close()
                    return@setOnClickListener
                }
                clickItem.invoke(list[layoutPosition])
            }

            tvDelete.setOnClickListener {
                slideLayout.close()
                deleteItem(layoutPosition)
            }
        }
    }

    private fun deleteItem(index:Int){
        val deleteBookmark = StorageUtil.deleteBookmark(list[index])
        if (deleteBookmark==1){
            list.removeAt(index)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyView {
        return MyView(LayoutInflater.from(ctx).inflate(R.layout.item_bookmark,parent,false))
    }

    override fun onBindViewHolder(holder: MyView, position: Int) {
        with(holder.itemView){
            val bookmarkBean = list[position]
            tv_title.text= bookmarkBean.title
            tv_url.text=bookmarkBean.web
            Glide.with(ctx)
                .load(bookmarkBean.icon)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(10)))
                .into(iv_logo)
            slide_layout.setOpen(bookmarkBean.open,false)
            slide_layout.setOnStateChangeListener(object : SlideLayout.OnStateChangeListener(){
                override fun onInterceptTouchEvent(layout: SlideLayout?): Boolean {
                    slideHelper.closeAll(layout)
                    return false
                }
                override fun onStateChanged(layout: SlideLayout?, open: Boolean) {
                    bookmarkBean.open=open
                    slideHelper.onStateChanged(layout, open)
                }
            })
        }
    }

    override fun getItemCount(): Int = list.size
}