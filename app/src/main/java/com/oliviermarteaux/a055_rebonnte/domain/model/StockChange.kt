package com.oliviermarteaux.a055_rebonnte.domain.model

import com.oliviermarteaux.shared.extensions.toDate
import com.oliviermarteaux.shared.firebase.authentication.domain.model.User
import java.io.Serializable
import java.net.MulticastSocket
import java.time.LocalDate
import java.util.Date
import java.util.UUID

data class MedicineChange(

    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val description: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val author: User? = User(),
    val date: Date? = LocalDate.now().toDate(),
    val time: Date? = LocalDate.now().toDate(),
    val previousStock: Int = 0,
    val newStock: Int = 0

): Serializable


enum class MedicineChangeType (title: String) {
    CREATION("Creation"), STOCK_CHANGE("Stock modification")
}