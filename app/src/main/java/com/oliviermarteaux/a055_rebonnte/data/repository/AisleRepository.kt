package com.oliviermarteaux.a055_rebonnte.data.repository

import com.oliviermarteaux.a055_rebonnte.domain.model.Aisle
import kotlinx.coroutines.flow.Flow

interface AisleRepository {

    fun getAislesSortedByDescTimestamp(): Flow<Result<List<Aisle>>>
    suspend fun addAisle(aisle: Aisle): Result<Unit>
}