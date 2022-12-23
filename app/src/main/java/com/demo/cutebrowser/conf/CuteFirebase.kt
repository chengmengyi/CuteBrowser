package com.demo.cutebrowser.conf

import com.demo.cutebrowser.admob.CuteAdNum
import com.demo.cutebrowser.bean.VpnBean
import com.demo.cutebrowser.util.cuteLog
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.get
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.lzy.okgo.OkGo
import com.lzy.okgo.callback.StringCallback
import com.lzy.okgo.model.Response
import com.tencent.mmkv.MMKV
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

object CuteFirebase {
    var csb_reffer="1"
    var csb_shpop="2"
    var csb_test=""
    var isIR=false
    val cityList= arrayListOf<String>()
    val vpnList= arrayListOf<VpnBean>()

    fun firebase(){
        CuteAdNum.getCurrent()
        createVpn(CuteConf.localVpnList)

//        val remoteConfig = Firebase.remoteConfig
//        remoteConfig.fetchAndActivate().addOnCompleteListener {
//            if (it.isSuccessful){
//                parseAdJson(remoteConfig.getString("cute_ad"))
//                parseCityJson(remoteConfig.getString("csb_fastest"))
//                parseVpnJson(remoteConfig.getString("csb_allservers"))
//
//                val csb_reffer = remoteConfig.getString("csb_reffer")
//                if (csb_reffer.isNotEmpty()){
//                    this.csb_reffer=csb_reffer
//                }
//
//                val csb_shpop = remoteConfig.getString("csb_shpop")
//                if (csb_shpop.isNotEmpty()){
//                    this.csb_shpop=csb_shpop
//                }
//
//                val csb_test = remoteConfig.getString("csb_test")
//                if (csb_test.isNotEmpty()){
//                    this.csb_test=csb_test
//                }
//            }
//        }
    }

    private fun parseCityJson(json:String){
        try {
            val jsonArray = JSONObject(json).getJSONArray("csb_fastest")
            for (index in 0 until jsonArray.length()){
                cityList.add(jsonArray.optString(index))
            }
        }catch (e:Exception){

        }
    }

    private fun parseVpnJson(json: String){
        try {
            val jsonArray = JSONObject(json).getJSONArray("csb_allservers")
            for (index in 0 until jsonArray.length()){
                val jsonObject = jsonArray.getJSONObject(index)
                vpnList.add(VpnBean(
                    jsonObject.optString("csb_acc"),
                    jsonObject.optInt("csb_ppo"),
                    jsonObject.optString("csb_pass"),
                    jsonObject.optString("csb_ccount"),
                    jsonObject.optString("csb_ccity"),
                    jsonObject.optString("csb_ip"),
                ))
            }
            createVpn(vpnList)
        }catch (e:Exception){

        }
    }

    private fun parseAdJson(json:String){
        CuteAdNum.getMax(json)
        MMKV.defaultMMKV().encode("cute_ad",json)
    }

    private fun createVpn(list:ArrayList<VpnBean>){
        list.forEach { it.createVpn() }
    }

    fun getStorageAd():String{
        val cuteAd = MMKV.defaultMMKV().decodeString("cute_ad")
        return if (cuteAd.isNullOrEmpty()){
            CuteConf.CUTE_AD
        }else{
            cuteAd
        }
    }

    fun checkIR(){
        val country = Locale.getDefault().country
        if(country=="IR"){
            isIR=true
        }else{
            OkGo.get<String>("https://api.myip.com/")
                .execute(object : StringCallback(){
                    override fun onSuccess(response: Response<String>?) {
//                        ipJson="""{"ip":"89.187.185.11","country":"United States","cc":"IR"}"""
                        try {
                            isIR=JSONObject(response?.body()?.toString()).optString("cc")=="IR"
                        }catch (e:Exception){

                        }
                    }

                    override fun onError(response: Response<String>?) {
                        super.onError(response)
                    }
                })
        }
    }
}