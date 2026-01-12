package com.oliviermarteaux.a055_rebonnte.domain.model

import com.oliviermarteaux.shared.extensions.toDate
import com.oliviermarteaux.shared.firebase.authentication.domain.model.User
import com.oliviermarteaux.shared.firebase.firestore.domain.model.Address
import com.oliviermarteaux.shared.firebase.firestore.domain.model.Comment
import java.io.Serializable
import java.time.LocalDate
import java.util.Date
import java.util.UUID

data class Aisle(

    val id: String = UUID.randomUUID().toString(),
    val name: String = "Default aisle name",
    val timestamp: Long = System.currentTimeMillis(),
    val author: User? = User(),
    val creationDate: Date? = LocalDate.now().toDate(),
    val creationTime: Date? = LocalDate.now().toDate(),

    ) : Serializable