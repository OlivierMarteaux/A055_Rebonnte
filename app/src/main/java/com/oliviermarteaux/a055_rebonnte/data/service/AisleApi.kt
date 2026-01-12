package com.oliviermarteaux.a055_rebonnte.data.service

import com.oliviermarteaux.a055_rebonnte.domain.model.Aisle
import kotlinx.coroutines.flow.Flow

interface AisleApi {

    fun getAislesSortedByDescTimestamp(): Flow<Result<List<Aisle>>>
    suspend fun addAisle(aisle: Aisle): Result<Unit>
}