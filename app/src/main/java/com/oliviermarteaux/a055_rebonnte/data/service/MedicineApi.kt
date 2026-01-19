package com.oliviermarteaux.a055_rebonnte.data.service

import com.google.firebase.firestore.DocumentSnapshot
import com.oliviermarteaux.a055_rebonnte.domain.model.Medicine
import com.oliviermarteaux.a055_rebonnte.ui.screen.MedicineSortOption
import com.oliviermarteaux.a055_rebonnte.ui.screen.PagedList
import kotlinx.coroutines.flow.Flow

interface MedicineApi {

//    fun getMedicineSortedByDescTimestamp(): Flow<Result<List<Medicine>>>
//    fun getMedicineSortedAndFilteredBy(query: String, medicineSortOption: MedicineSortOption): Flow<Result<List<Medicine>>>
    fun getMedicinesFilteredSortedPaged(
        query: String,
        medicineSortOption: MedicineSortOption,
        pageSize: Long,
        lastSnapshot: DocumentSnapshot?
    ): Flow<Result<PagedList<Medicine>>>
    suspend fun addMedicine(medicine: Medicine): Result<Unit>
    suspend fun updateMedicine(medicine: Medicine): Result<Unit>
    suspend fun deleteMedicine(medicineId: String): Result<Unit>
}