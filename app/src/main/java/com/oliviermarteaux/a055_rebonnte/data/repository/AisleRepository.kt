package com.oliviermarteaux.a055_rebonnte.data.repository

import com.google.firebase.firestore.DocumentSnapshot
import com.oliviermarteaux.a055_rebonnte.domain.model.Aisle
import com.oliviermarteaux.a055_rebonnte.ui.PagedList
import kotlinx.coroutines.flow.Flow

interface AisleRepository {

    fun getAislePaged(
        pageSize: Long,
        lastSnapshot: DocumentSnapshot?
    ): Flow<Result<PagedList<Aisle>>>
    fun getAislesSortedByDescTimestamp(): Flow<Result<List<Aisle>>>
    suspend fun addAisle(aisle: Aisle): Result<Unit>
}