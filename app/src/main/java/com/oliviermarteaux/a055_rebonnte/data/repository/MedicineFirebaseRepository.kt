package com.oliviermarteaux.a055_rebonnte.data.repository

import com.google.firebase.firestore.DocumentSnapshot
import com.oliviermarteaux.a055_rebonnte.data.service.MedicineApi
import com.oliviermarteaux.a055_rebonnte.domain.model.Medicine
import com.oliviermarteaux.a055_rebonnte.ui.MedicineSortOption
import com.oliviermarteaux.a055_rebonnte.ui.PagedList
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MedicineFirebaseRepository @Inject constructor(
    private val medicineApi: MedicineApi,
): MedicineRepository {

//    override fun getMedicineSortedByDescTimestamp(): Flow<Result<List<Medicine>>> =
//        medicineApi.getMedicineSortedByDescTimestamp()
//    override fun getMedicineSortedAndFilteredBy(query: String, medicineSortOption: MedicineSortOption): Flow<Result<List<Medicine>>> =
//        medicineApi.getMedicineSortedAndFilteredBy(query, medicineSortOption)

    override fun getMedicinesFilteredSortedPaged(
        query: String,
        aisleId: String,
        medicineSortOption: MedicineSortOption,
        pageSize: Long,
        lastSnapshot: DocumentSnapshot?
    ): Flow<Result<PagedList<Medicine>>> =
        medicineApi.getMedicinesFilteredSortedPaged(
            query = query,
            aisleId = aisleId,
            medicineSortOption = medicineSortOption,
            pageSize = pageSize,
            lastSnapshot = lastSnapshot
        )

    override suspend fun addMedicine(medicine: Medicine): Result<Unit> =
        medicineApi.addMedicine(medicine)

    override suspend fun updateMedicine(medicine: Medicine): Result<Unit> =
        medicineApi.updateMedicine(medicine)

    override suspend fun deleteMedicine(medicineId: String): Result<Unit> =
        medicineApi.deleteMedicine(medicineId)
}