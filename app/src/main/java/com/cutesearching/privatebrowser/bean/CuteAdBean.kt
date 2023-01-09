package com.cutesearching.privatebrowser.bean

class CuteAdBean(
    val id_cute:String,
    val type_cute:String,
    val sort_cute:Int,
    val source_cute:String,
) {
    override fun toString(): String {
        return "CuteAdBean(id_cute='$id_cute', type_cute='$type_cute', sort_cute=$sort_cute, source_cute='$source_cute')"
    }
}