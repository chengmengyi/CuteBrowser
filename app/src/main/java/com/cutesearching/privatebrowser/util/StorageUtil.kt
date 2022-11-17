package com.cutesearching.privatebrowser.util

import android.content.Context
import android.net.Uri
import com.cutesearching.privatebrowser.bean.BookmarkBean
import com.cutesearching.privatebrowser.bean.HistoryBean
import org.litepal.LitePal
import java.lang.Exception

object StorageUtil {

    fun saveBookmark(context: Context, title:String, url:String){
        try {
            val find = LitePal.select("*")
                .where("web = ?", url)
                .order("time desc")
                .limit(200)
                .offset(0)
                .find(BookmarkBean::class.java)
            if (find?.isEmpty() == true){
                val parse = Uri.parse(url)
                val logo="${parse.scheme}://${parse.host}/favicon.ico"
                val save = BookmarkBean(title, url, logo, time = System.currentTimeMillis()).save()
                context.showToast(if (save) "Successfully added" else "Add failed")
            }
        }catch (e: Exception){
            cuteLog("==${e.message}==")
        }
    }

    fun queryBookmark(offset:Int):List<BookmarkBean>{
        return LitePal.select("*")
            .order("time desc")
            .limit(20)
            .offset(offset)
            .find(BookmarkBean::class.java)
    }

    fun searchBookmark(offset: Int,content:String): List<BookmarkBean> {
        return LitePal.select("*")
            .where("title like ? or web like ?", "%$content%", "%$content%")
            .order("time desc")
            .limit(20)
            .offset(offset)
            .find(BookmarkBean::class.java)
    }

    fun deleteBookmark(bean: BookmarkBean): Int {
        return LitePal.deleteAll(BookmarkBean::class.java, "time = ?", "${bean.time}")
    }

    fun saveHistory(title:String, url:String){
        try {
            val find = LitePal.select("*")
                .where("web = ?", url)
                .order("time desc")
                .limit(200)
                .offset(0)
                .find(HistoryBean::class.java)
            if (find?.isEmpty() == true){
                val parse = Uri.parse(url)
                val logo="${parse.scheme}://${parse.host}/favicon.ico"
                HistoryBean(title, url, logo, time = System.currentTimeMillis()).save()
            }
        }catch (e: Exception){
            cuteLog("==${e.message}==")
        }
    }

    fun queryHistory(offset: Int): List<HistoryBean> {
        return LitePal.select("*")
            .order("time desc")
            .limit(20)
            .offset(offset)
            .find(HistoryBean::class.java)
    }

    fun searchHistory(offset: Int,content:String): List<HistoryBean> {
        return LitePal.select("*")
            .where("title like ? or web like ?", "%$content%", "%$content%")
            .order("time desc")
            .limit(20)
            .offset(offset)
            .find(HistoryBean::class.java)
    }
}