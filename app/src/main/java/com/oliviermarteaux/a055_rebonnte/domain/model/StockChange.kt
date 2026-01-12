package com.oliviermarteaux.a055_rebonnte.domain.model

import com.oliviermarteaux.shared.firebase.authentication.domain.model.User
import java.io.Serializable
import java.util.Date
import java.util.UUID

data class StockChange(

    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val description: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val author: User? = User(),
    val date: Date? = null,
    val time: Date? = null,
    val previousStock: Int = 0,
    val newStock: Int = 0

): Serializable
