package com.oliviermarteaux.a055_rebonnte.data.service

import com.google.firebase.firestore.DocumentSnapshot
import com.oliviermarteaux.a055_rebonnte.domain.model.Aisle
import com.oliviermarteaux.a055_rebonnte.domain.model.Medicine
import com.oliviermarteaux.a055_rebonnte.ui.screen.MedicineSortOption
import com.oliviermarteaux.a055_rebonnte.ui.screen.PagedList
import kotlinx.coroutines.flow.Flow

interface AisleApi {

    fun getAislePaged(
        pageSize: Long,
        lastSnapshot: DocumentSnapshot?
    ): Flow<Result<PagedList<Aisle>>>
    fun getAislesSortedByDescTimestamp(): Flow<Result<List<Aisle>>>
    suspend fun addAisle(aisle: Aisle): Result<Unit>
}