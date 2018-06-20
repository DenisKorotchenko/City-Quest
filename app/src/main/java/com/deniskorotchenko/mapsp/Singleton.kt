package com.deniskorotchenko.mapsp

import java.util.*

class Singleton private constructor() {
    init { /*println("This ($this) is a singleton")*/ }
    private object Holder { val INSTANCE = Singleton() }
    companion object {
        val instance: Singleton by lazy { Holder.INSTANCE }
    }
    var nowQuestion : Int = 0
    var startTime : Long = 0
}