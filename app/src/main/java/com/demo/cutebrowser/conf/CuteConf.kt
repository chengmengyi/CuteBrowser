package com.demo.cutebrowser.conf

import com.demo.cutebrowser.bean.VpnBean

object CuteConf {
    const val OPEN="cute_open"
    const val BOOK_MARK="cute_bookmark"
    const val HISTORY="cute_history"
    const val TAB="cute_tab"
    const val HOME="csb_home"
    const val VPN_HOME="csb_vpnhome"
    const val VPN_RESULT="csb_resu"
    const val VPN_CONNECT="csb_int_all"
    const val VPN_BACK="csb_return_int"

    val localVpnList=arrayListOf(
        VpnBean(
            csb_acc = "chacha20-ietf-poly1305",
            csb_ppo = 100,
            csb_pass = "123456",
            csb_ccount = "Japan",
            csb_ccity = "Tokyo222",
            csb_ip = "100.223.52.0"
        ),
        VpnBean(
            csb_acc = "chacha20-ietf-poly1305",
            csb_ppo = 100,
            csb_pass = "123456",
            csb_ccount = "UnitedStates",
            csb_ccity = "Tokyo",
            csb_ip = "100.223.52.78"
        ),
    )

    const val CUTE_AD="""{
     "cute_click":15,
    "cute_show":50,
    "cute_open": [
        {
            "cute_source": "admob",
            "cute_id": "ca-app-pub-3940256099942544/3419835294",
            "cute_type": "kai",
            "cute_sort": 1
        }
    ],
    "cute_bookmark": [
       {
            "cute_source": "admob",
            "cute_id": "ca-app-pub-3940256099942544/8691691433A",
            "cute_type": "cha",
            "cute_sort": 2
        },
        {
            "cute_source": "admob",
            "cute_id": "ca-app-pub-3940256099942544/1033173712",
            "cute_type": "cha",
            "cute_sort": 1
        },
        {
            "cute_source": "admob",
            "cute_id": "ca-app-pub-3940256099942544/1033173712AA",
            "cute_type": "cha",
            "cute_sort": 3
        }
    ],
     "cute_history": [
       {
            "cute_source": "admob",
            "cute_id": "ca-app-pub-3940256099942544/2247696110A",
            "cute_type": "yuan",
            "cute_sort": 2
        },
        {
            "cute_source": "admob",
            "cute_id": "ca-app-pub-3940256099942544/2247696110",
            "cute_type": "yuan",
            "cute_sort": 1
        },
        {
            "cute_source": "admob",
            "cute_id": "ca-app-pub-3940256099942544/2247696110AA",
            "cute_type": "yuan",
            "cute_sort": 3
        }
    ],
      "csb_home": [
        {
            "cute_source": "admob",
            "cute_id": "ca-app-pub-3940256099942544/2247696110",
            "cute_type": "yuan",
            "cute_sort": 1
        }
    ],
      "csb_vpnhome": [
        {
            "cute_source": "admob",
            "cute_id": "ca-app-pub-3940256099942544/2247696110",
            "cute_type": "yuan",
            "cute_sort": 1
        }
    ],
      "csb_resu": [
        {
            "cute_source": "admob",
            "cute_id": "ca-app-pub-3940256099942544/2247696110",
            "cute_type": "yuan",
            "cute_sort": 1
        }
    ],
      "csb_int_all": [
        {
            "cute_source": "admob",
            "cute_id": "ca-app-pub-3940256099942544/1033173712",
            "cute_type": "cha",
            "cute_sort": 1
        }
    ],
      "csb_return_int": [
        {
            "cute_source": "admob",
            "cute_id": "ca-app-pub-3940256099942544/1033173712",
            "cute_type": "cha",
            "cute_sort": 1
        }
    ],
    "cute_tab": [
        {
            "cute_source": "admob",
            "cute_id": "ca-app-pub-3940256099942544/8691691433A",
            "cute_type": "cha",
            "cute_sort": 2
        },
        {
            "cute_source": "admob",
            "cute_id": "ca-app-pub-3940256099942544/1033173712",
            "cute_type": "cha",
            "cute_sort": 1
        },
        {
            "cute_source": "admob",
            "cute_id": "ca-app-pub-3940256099942544/1033173712AA",
            "cute_type": "cha",
            "cute_sort": 3
        }
    ]
}"""
}