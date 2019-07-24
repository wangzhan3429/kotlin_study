package com.ceshui.mh.utils

import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPool

class SubThread(private val jedisPool: JedisPool) : Thread("SubThread") {
    private val subscriber = ClientSubscriber()

    private val channel = "mychannel"

    init {

    }

    override fun run() {
        println(String.format("subscribe redis, channel %s, thread will be blocked", channel))
        var jedis: Jedis? = null
        try {
            jedis = jedisPool.resource   //取出一个连接
            jedis!!.subscribe(subscriber, channel)    //通过subscribe 的api去订阅，入参是订阅者和频道名
        } catch (e: Exception) {
            println(String.format("subsrcibe channel error, %s", e))
        } finally {
            jedis?.close()
        }
    }


}