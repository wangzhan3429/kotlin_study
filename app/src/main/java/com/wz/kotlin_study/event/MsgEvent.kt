package com.ceshui.mh.event

/**
 * Created by wangzhan on 2019-07-16 11:26
 */
class MsgEvent {
    var type: Int = 0
    var msg :String = ""

    constructor(type: Int) {
        this.type = type
    }

    constructor(type: Int, msg: String) {
        this.type = type
        this.msg = msg
    }

}