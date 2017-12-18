package br.com.dusty.dkits.util

inline fun <reified T> Array<T>.add(element: T) = toMutableSet().run {
	val updated = add(element)
	Pair(toTypedArray(), updated)
}

inline fun <reified T> Array<T>.remove(element: T) = toMutableSet().run {
	val updated = remove(element)
	Pair(toTypedArray(), updated)
}
