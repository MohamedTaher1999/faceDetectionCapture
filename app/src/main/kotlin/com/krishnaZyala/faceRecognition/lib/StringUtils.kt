package com.krishnaZyala.faceRecognition.lib

object StringUtils {
    val String.spaced get():String = "(?<=.)[A-Z]".toRegex().replace(this) { " ${it.value}" }
}