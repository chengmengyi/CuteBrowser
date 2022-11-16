package com.demo.cutebrowser.bean

import org.litepal.crud.LitePalSupport

class HistoryBean(
    val title:String="",
    val web:String="",
    val icon:String="",
    var time:Long=0,
    val itemType:Int=10
): LitePalSupport()