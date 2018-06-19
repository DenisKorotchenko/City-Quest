package com.deniskorotchenko.mapsp

/**
 * Created by user on 6/19/18.
 */
class Singleton private constructor() {
    init { /*println("This ($this) is a singleton")*/ }
    private object Holder { val INSTANCE = Singleton() }
    companion object {
        val instance: Singleton by lazy { Holder.INSTANCE }
    }
    var nowQuestion : Int = 0
}