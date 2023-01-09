package com.cutesearching.privatebrowser.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cutesearching.privatebrowser.R
import com.cutesearching.privatebrowser.manager.BrowserManager
import com.cutesearching.privatebrowser.util.show
import kotlinx.android.synthetic.main.item_bottom.view.*

class BottomAdapter(
    private val context: Context,
    private val click:(index:Int)->Unit
):RecyclerView.Adapter<BottomAdapter.MyView>() {
    private val list= arrayListOf<Int>()
    private var tabSize=1

    init {
        list.clear()
        list.add(R.drawable.bottom1)
        list.add(R.drawable.bottom2)
        list.add(R.drawable.bottom3)
        list.add(R.drawable.bottom4)
        list.add(R.drawable.bottom5)
    }

    fun updateTabSize(){
        tabSize=BrowserManager.tabList.size
        notifyDataSetChanged()
    }

    inner class MyView(view:View):RecyclerView.ViewHolder(view){
        init {
            view.setOnClickListener { click.invoke(layoutPosition) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyView {
        return MyView(LayoutInflater.from(context).inflate(R.layout.item_bottom,parent,false))
    }

    override fun onBindViewHolder(holder: MyView, position: Int) {
        with(holder.itemView){
            tv_num.show(position==3)
            tv_num.text="$tabSize"
            iv_bottom.setImageResource(list[position])

        }
    }

    override fun getItemCount(): Int = list.size
}