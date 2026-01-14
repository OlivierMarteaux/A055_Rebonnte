package com.oliviermarteaux.a055_rebonnte.data.service

import com.oliviermarteaux.a055_rebonnte.domain.model.Medicine
import kotlinx.coroutines.flow.Flow

interface MedicineApi {

    fun getMedicineSortedByDescTimestamp(): Flow<Result<List<Medicine>>>
    suspend fun addMedicine(medicine: Medicine): Result<Unit>
    suspend fun updateMedicine(medicine: Medicine): Result<Unit>
}