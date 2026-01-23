package com.oliviermarteaux.localshared.extensions

import android.util.Log
import com.google.android.play.integrity.internal.z

fun String.toShiftedAlpha(): String {
    if (this.isEmpty()) return "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz"

    val newString = when (val lastChar = last()) {
        in '0'..'y' -> dropLast(1) + (lastChar + 1) // normal increment
        'z' -> this + '0'                            // append '0' if last char is 'z'
        else -> dropLast(1) + lastChar               // leave unchanged for any other char
    }

    Log.d("OM_TAG", "$this -> $newString")
    return newString
}