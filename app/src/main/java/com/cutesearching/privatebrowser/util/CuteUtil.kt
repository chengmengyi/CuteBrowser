package com.cutesearching.privatebrowser.util

import android.util.Log
import com.cutesearching.privatebrowser.bean.CuteAdBean
import com.cutesearching.privatebrowser.conf.CuteFirebase
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

fun cuteLog(string: String){
    Log.e("qwer",string)
}

fun formatTime(time:Long):String{
    return try {
        SimpleDateFormat("yyyy-MM-dd").format(Date(time))
    }catch (e:Exception){
        "time error"
    }
}


fun getAdList(t:String):List<CuteAdBean>{
    val list= arrayListOf<CuteAdBean>()
    try {
        val jsonArray = JSONObject(CuteFirebase.getStorageAd()).getJSONArray(t)
        for (index in 0 until jsonArray.length()){
            val jsonObject = jsonArray.getJSONObject(index)
            list.add(
                CuteAdBean(
                    jsonObject.optString("cute_id"),
                    jsonObject.optString("cute_type"),
                    jsonObject.optInt("cute_sort"),
                    jsonObject.optString("cute_source"),
                )
            )
        }
    }catch (e:Exception){}
    return list.filter { it.source_cute == "admob" }.sortedByDescending { it.sort_cute }
}