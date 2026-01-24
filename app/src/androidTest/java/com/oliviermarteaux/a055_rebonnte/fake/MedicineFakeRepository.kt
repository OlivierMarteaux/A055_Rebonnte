package com.oliviermarteaux.a055_rebonnte.fake

import com.google.firebase.firestore.DocumentSnapshot
import com.oliviermarteaux.a055_rebonnte.data.fake.fakeAisleList
import com.oliviermarteaux.a055_rebonnte.data.fake.fakeMedicineList
import com.oliviermarteaux.a055_rebonnte.data.repository.MedicineRepository
import com.oliviermarteaux.a055_rebonnte.domain.model.Aisle
import com.oliviermarteaux.a055_rebonnte.domain.model.Medicine
import com.oliviermarteaux.a055_rebonnte.ui.MedicineSortOption
import com.oliviermarteaux.a055_rebonnte.ui.PagedList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class MedicineFakeRepository: MedicineRepository {

    var fakeList: PagedList<Medicine> = PagedList(
        items = fakeMedicineList,
        lastSnapshot = null,
        isLastPage = false
    )

    override fun getMedicinesFilteredSortedPaged(
        query: String,
        aisleId: String,
        medicineSortOption: MedicineSortOption,
        pageSize: Long,
        lastSnapshot: DocumentSnapshot?
    ): Flow<Result<PagedList<Medicine>>> = flowOf(Result.success(fakeList))

    override suspend fun addMedicine(medicine: Medicine): Result<Unit> {

        val newFakeList = listOf(medicine) + fakeMedicineList
        fakeList = fakeList.copy(items = newFakeList)
        return Result.success(Unit)
    }

    override suspend fun updateMedicine(medicine: Medicine): Result<Unit> = Result.success(Unit)

    override suspend fun deleteMedicine(medicineId: String): Result<Unit> {
        fakeList = fakeList.copy(
            items = fakeList.items.filterNot { it.id == medicineId }
        )
        return Result.success(Unit)
    }

}