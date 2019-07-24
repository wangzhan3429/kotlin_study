package com.ceshui.mh.utils

import com.ceshui.mh.Const
import com.ceshui.mh.event.NetEvent
import org.greenrobot.eventbus.EventBus
import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPool as JedisPool1
import redis.clients.jedis.JedisPool
import java.util.concurrent.ConcurrentHashMap


/**
 * Created by wangzhan on 2019-07-16 17:54
 */

class ServerJedis { // JEDIS 对象是否需要新的
    private lateinit var jedis: Jedis
    private var connected: Boolean = true

    //Redis的端口号
    private val PORT = 6379


    fun connect(ip: String) { // 192.168.1.103
        KLog.d("----connect server redis ip---" + ip)
        try {
            jedis = Jedis(ip)
            jedis.connect()
            KLog.d("----waiting server connect---" + ip)
        } catch (e: Exception) {
            jedis.close()
            connected = false
            EventBus.getDefault().post(NetEvent(Const.SERVER_CONNECT_FAILED))
            KLog.d("------connect server failed---" + ip) // 异常的地方，try catch，然后重新访问网络，请求新的ip地址
        } finally {
            if (connected) {
                KLog.d("-----subscribe--server succeed-----")
                EventBus.getDefault().post(NetEvent(Const.SERVER_CONNECT_SUCCEED))
            }
        }
    }


    //  客户端publish消息
    fun publish(message: String) {
        jedis.publish(Const.PAD, message)
    }


//    /**
//     * 获取连接池.
//     */
//    private fun getPool(ip: String): JedisPool? {
//        val key = ip + PORT
//        var pool: JedisPool? = null
//        if (!maps.containsKey(key)) {//根据ip和端口判断连接池是否存在.
//            val config = JedisPoolConfig()
//            config.maxTotal = MAX_ACTIVE
//            config.maxIdle = MAX_IDLE
//            config.maxWaitMillis = MAX_WAIT.toLong()
//            config.testOnBorrow = TEST_ON_BORROW
//            config.testOnReturn = TEST_ON_RETURN
//            try {
//                pool = JedisPool(config, ip)
//
//                maps.put(key, pool)
//            } catch (e: Exception) {
//                KLog.e("初始化Redis连接池异常:", e)
//            }
//        } else {
//            pool = maps.get(key)
//        }
//        return pool
//    }


}