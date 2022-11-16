package com.demo.cutebrowser.bean

import org.litepal.crud.LitePalSupport

class BookmarkBean(
    val title:String="",
    val web:String="",
    val icon:String="",
    var time:Long=0,
    var open:Boolean=false
): LitePalSupport()