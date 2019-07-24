package com.ceshui.mh.utils

/**
 * Created by wangzhan on 2019-07-23 16:23
 */
object ClientObj {
    private lateinit var clientJedis: ClientJedis
    private lateinit var serverJedis: ServerJedis


    fun setClientJ(clientJedis: ClientJedis) {
        this.clientJedis = clientJedis
    }

    fun getClientJ() = clientJedis

    fun setServerJ(serverJedis: ServerJedis) {
        this.serverJedis = serverJedis
    }

    fun getServerJ() = serverJedis



}