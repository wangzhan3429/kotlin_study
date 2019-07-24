package com.wz.kotlin_study.part01

/**
 * Created by wangzhan on 2019-07-08 15:42
 */

object OpenTest {
    fun test() {

    }


    // 扩展函数
    fun MutableList<Int>.swap(num1: Int, num2: Int) {
        var temp = this[num1]
        this[num1] = this[num2]
        this[num2] = temp

    }

    // 操作符
    operator fun get(a: Int, b: Int): Int {
        return a + b
    }


    interface classT {
        fun add(a: Int, b: Int): Int
    }

    // 枚举类
    enum class Color {
        YELLOW, RED, BLACK, WHITE
    }


    // 枚举类
    enum class Opera(color: Int) {
        BLACK(color = 123),
        YELLOW(222),
        RED(333)
    }

    // 枚举  可以实现接口，但是不能继承类
    enum class InterTest : classT {
        ADD {
            override fun add(a: Int, b: Int): Int {
                OpenTest.Opera.values() // 返回所有的枚举值
                return a + b
            }
        },
        DELETE {
            override fun add(a: Int, b: Int) = a - b
        }

    }

}