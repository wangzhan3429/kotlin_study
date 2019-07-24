package com.wz.kotlin_study.part01

import java.util.Arrays.asList

/**
 * Created by wangzhan on 2019-06-28 10:39
 *
 *
 * 循环语句，if、when、while、for
 * break  continue
 *
 *
 *
 * while和do{}while 和java中的类似
 *
 *
 *
 *
 */


class class_20190629 {

    fun foo() {
        listOf(1, 2, 3, 4, 5).forEach {
            if (it == 3) // 局部返回到该 lambda 表达式的调用者，即 forEach 循环
                System.out.println(it)
        }
        System.out.println(" ---done with explicit label")
    }

    fun testFor() {
        var s: Int = 0
        for (i in 1..100) {
            s += i
        }
        System.out.println(s)

        for (s in asList(1, 2, 3)) {
            System.out.println(s)
        }

        // downTo 是降序  默认是升序  step是步伐
        for (i in 6 downTo 0 step 2) {
            System.out.println(i)
        }

        // 库函数  withIndex  返回的是一个键值对的对象
        var l = intArrayOf(1, 2, 3)
        for ((index, value) in l.withIndex()) {
            println("---" + index + "==" + value)
        }
    }

    fun testWhile() {
        var i = 9
        while (i > 0) {
            i--
        }
        println("-----" + i)

        do {
            i--
        } while (i > 0)
    }

    fun testWhen() {
        var i = 9
        when (i) {
            1 -> {
                println("1")
            }
            2 -> {
                print("2'")
            }
            9 -> {
                print("3")
            }
            else -> {
                print("else...")
            }
        }

        when(i){
            in 1..5 -> print("in i .. 5")
            !in 10..20 -> print("!in 10..20")
            else-> print("else")
        }
    }

    fun testType(type:Any){
        when(type){ // 智能类型转换，判断为string，则可以按照string来操作
            is String -> type.startsWith("sss")
            is Int -> type.and(3)
        }
    }

    fun test(x:Any){

    }

}