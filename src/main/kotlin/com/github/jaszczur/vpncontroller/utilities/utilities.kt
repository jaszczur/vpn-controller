package com.github.jaszczur.vpncontroller.utilities

import java.util.*

class Window<T>(val maxSize: Int) {
    private val values = LinkedList<T>()

    fun add(elem: T) {
        synchronized(values) {
            if (values.size == maxSize) {
                values.removeLast()
            }
            values.addFirst(elem)
        }
    }

    fun values() = synchronized(values) { values.toList() }
}