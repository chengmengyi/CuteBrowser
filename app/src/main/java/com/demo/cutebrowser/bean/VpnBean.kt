package com.demo.cutebrowser.bean

import com.github.shadowsocks.database.Profile
import com.github.shadowsocks.database.ProfileManager

class VpnBean(
    val csb_acc:String="", //账号
    val csb_ppo:Int=0, //端口
    val csb_pass:String="", //密码
    val csb_ccount:String="Super Fast Server", //国家
    val csb_ccity:String="", //城市
    val csb_ip:String="" //ip
) {

    fun isFast()=csb_ip.isEmpty()&&csb_ccount=="Super Fast Server"

    fun getVpnId():Long{
        ProfileManager.getActiveProfiles()?.forEach {
            if (it.host==csb_ip&&it.remotePort==csb_ppo){
                return it.id
            }
        }
        return 0L
    }

    fun createVpn(){
        val profile = Profile(
            id = 0L,
            name = "${csb_ccount} - ${csb_ccity}",
            host = csb_ip,
            remotePort = csb_ppo,
            password = csb_pass,
            method = csb_acc
        )

        var id: Long? = null
        ProfileManager.getActiveProfiles()?.forEach {
            if (it.remotePort == profile.remotePort && it.host == profile.host) {
                id = it.id
                return@forEach
            }
        }
        if (null == id) {
            ProfileManager.createProfile(profile)
        } else {
            profile.id = id!!
            ProfileManager.updateProfile(profile)
        }
    }
}