package br.com.dusty.dkits.util.stdlib

import br.com.dusty.dkits.Main

fun Long.millisToPeriod() = Math.round((this - System.currentTimeMillis()) / 1000.0)

fun Long.millisToSeconds() = Math.round(this / 1000.0)

fun Double.chances() = Main.RANDOM.nextFloat() < this

fun Int.round(divider: Double) = Math.round(this / divider) * divider
