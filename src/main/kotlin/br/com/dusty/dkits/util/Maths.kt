package br.com.dusty.dkits.util

import br.com.dusty.dkits.Main

fun Long.millisToSeconds() = Math.round((this - System.currentTimeMillis()) / 1000.0)

fun Double.chances() = Main.RANDOM.nextFloat() < this
