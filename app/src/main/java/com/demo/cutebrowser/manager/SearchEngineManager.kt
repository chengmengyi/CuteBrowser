package com.demo.cutebrowser.manager

object SearchEngineManager {

    fun getSearchUrl(content:String):String{
        return "https://www.google.com/search?client=mysearch&ie=UTF-8&oe=UTF-8&q=$content"
    }
}