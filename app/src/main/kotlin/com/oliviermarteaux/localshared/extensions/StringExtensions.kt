package com.oliviermarteaux.localshared.extensions

import com.google.common.io.Files.map

fun String.toShiftedAlpha(): String {
    if (this.isEmpty()) return "z"

    val shiftedLastChar = when ( val lastChar = last()) {
        in 'a'..'y' -> lastChar + 1
        'z' -> 'z'
        in 'A'..'Y' -> lastChar + 1
        'Z' -> 'Z'
        else -> lastChar
    }

    return dropLast(1) + shiftedLastChar
}