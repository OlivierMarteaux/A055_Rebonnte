package com.oliviermarteaux.a055_rebonnte.fake

import com.google.firebase.firestore.DocumentSnapshot
import com.oliviermarteaux.a055_rebonnte.data.fake.fakeMedicineList
import com.oliviermarteaux.a055_rebonnte.data.repository.MedicineRepository
import com.oliviermarteaux.a055_rebonnte.domain.model.Medicine
import com.oliviermarteaux.a055_rebonnte.ui.MedicineSortOption
import com.oliviermarteaux.a055_rebonnte.ui.PagedList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class MedicineFakeRepository: MedicineRepository {

    override fun getMedicinesFilteredSortedPaged(
        query: String,
        aisleId: String,
        medicineSortOption: MedicineSortOption,
        pageSize: Long,
        lastSnapshot: DocumentSnapshot?
    ): Flow<Result<PagedList<Medicine>>> = flowOf(Result.success(PagedList(
        items = fakeMedicineList,
        lastSnapshot = null,
        isLastPage = true
    )))

    override suspend fun addMedicine(medicine: Medicine): Result<Unit> = Result.success(Unit)

    override suspend fun updateMedicine(medicine: Medicine): Result<Unit> = Result.success(Unit)

    override suspend fun deleteMedicine(medicineId: String): Result<Unit> = Result.success(Unit)

}