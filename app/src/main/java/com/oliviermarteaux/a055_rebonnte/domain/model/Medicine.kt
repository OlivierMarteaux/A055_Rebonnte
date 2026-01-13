package com.oliviermarteaux.a055_rebonnte.domain.model

import com.oliviermarteaux.shared.firebase.authentication.domain.model.User
import java.util.Date
import java.util.UUID
import java.io.Serializable

data class Medicine(

    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val aisle: Aisle = Aisle(),
    val stock: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val author: User? = User(),
    val creationDate: Date? = null,
    val creationTime: Date? = null,
    val changeRecord: List<StockChange> = emptyList()

): Serializable