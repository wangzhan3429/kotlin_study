package com.ceshui.mh.entry

/**
 * Created by wangzhan on 2019-07-23 10:09
 */
class IpBean {
    var error = ""
    var data: List<Data>? = null

    inner class Data {
        var public_ip = ""
        var private_ip = ""
    }
}