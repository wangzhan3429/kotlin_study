package com.ceshui.mh.event

/**
 * Created by wangzhan on 2019-07-16 11:26
 */
class NetEvent {
    var type: Int = 0
    var retry: Boolean = false
    var ip :String = ""

    constructor(type: Int) {
        this.type = type
    }

    constructor(type: Int, retry: Boolean) {
        this.type = type
        this.retry = retry
    }

    constructor(type: Int, ip: String) {
        this.type = type
        this.ip = ip
    }

}