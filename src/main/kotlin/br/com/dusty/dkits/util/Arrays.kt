package br.com.dusty.dkits.util

inline fun <reified T> Array<T>.add(element: T) = toMutableSet().run { Pair(toTypedArray(), add(element)) }

inline fun <reified T> Array<T>.remove(element: T) = toMutableSet().run { Pair(toTypedArray(), remove(element)) }
