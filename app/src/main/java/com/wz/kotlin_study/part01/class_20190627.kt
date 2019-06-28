package com.wz.kotlin_study.part01

import java.util.Arrays.asList

/**
 * Created by wangzhan on 2019-06-27 15:58
 *
 * kotlin 数组
 */

open class class_20190627 {
    // 函数用fun关键字声明


    // 函数定义  参数 不能有var 或者val
    fun greet(s: String) {
        print(s)
    }

    // Unit 类型，即没有返回值
    fun testUnit(): Unit {
        println("just test unit")
        System.out.println("just test unit")
    }

    // 函数后面如果不带返回值，默认是Unit类型，此时不能有返回值不能return
    fun testReturn(a: Int): Int {
        return a + 1
    }

    // 没有返回值，自动确定返回值
    fun testLamda(a: Int, b: Int) = a + b


    // b是默认参数
    open fun testDefault(a: Int, b: Int = 2) {

    }

    // 测试
    fun testSuper() {
//        class B : `class_20190627.kt`() { // `class_20190627.kt` 必须加上open修饰，不然不能被继承
//            override fun testDefault(a: Int, b: Int) { // 被重写覆盖的方法必须加上override
//
//            }
//        }

    }

    fun testVararg(vararg s: Int) {
        for (a in s) {
            System.out.println(a)
        }
    }


    // 测试函数调用
    fun testUse() {
//        testVararg(1, 2, 3)
//        testVararg(*asList(1, 2, 3))
        foo(strings = *arrayOf("a", "b", "c"))
    }

    fun foo(vararg strings: String) {}


}