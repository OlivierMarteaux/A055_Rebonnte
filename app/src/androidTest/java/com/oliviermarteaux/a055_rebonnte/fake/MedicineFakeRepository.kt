package com.oliviermarteaux.a055_rebonnte.fake

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.oliviermarteaux.a055_rebonnte.data.fake.fakeMedicineList
import com.oliviermarteaux.a055_rebonnte.data.repository.MedicineRepository
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
    ): Flow<Result<PagedList<Medicine>>> {

        if (query.isNotBlank()) {
            val newFakeList = fakeMedicineList.filter { it.name.contains(query, ignoreCase = true)  }
            fakeList = fakeList.copy(items = newFakeList)
        }

        when (medicineSortOption) {
            MedicineSortOption.ASCENDING_NAME -> {
                fakeList = fakeList.copy(items = fakeMedicineList)
                val newFakeList = fakeMedicineList.sortedBy { it.name }
                fakeList = fakeList.copy(items = newFakeList)
                for (i in 0..7) {Log.d("OM_TAG", "MedicineFakeRepository::getMedicineFilteredSortedPaged: ${newFakeList[i].name}: ${newFakeList[i].stock}")}
            }
            MedicineSortOption.ASCENDING_STOCK -> {
                fakeList = fakeList.copy(items = fakeMedicineList)
                val newFakeList = fakeMedicineList.sortedBy { it.stock }
                fakeList = fakeList.copy(items = newFakeList)
                for (i in 0..7) {Log.d("OM_TAG", "MedicineFakeRepository::getMedicineFilteredSortedPaged: ${newFakeList[i].name}: ${newFakeList[i].stock}")}
            }
            MedicineSortOption.DESCENDING_STOCK -> {
                fakeList = fakeList.copy(items = fakeMedicineList)
                val newFakeList = fakeMedicineList.sortedByDescending { it.stock }
                fakeList = fakeList.copy(items = newFakeList)
                for (i in 0..7) {Log.d("OM_TAG", "MedicineFakeRepository::getMedicineFilteredSortedPaged: ${newFakeList[i].name}: ${newFakeList[i].stock}")}
            }
            else -> {}
        }

        return flowOf(Result.success(fakeList))
    }

    override suspend fun addMedicine(medicine: Medicine): Result<Unit> {

        val newFakeList = listOf(medicine) + fakeMedicineList
        fakeList = fakeList.copy(items = newFakeList)
        return Result.success(Unit)
    }

    override suspend fun updateMedicine(medicine: Medicine): Result<Unit> {

        val newFakeList = listOf(medicine) + fakeMedicineList.filterNot { it.id == medicine.id }
        fakeList = fakeList.copy(items = newFakeList)
        return Result.success(Unit)
    }

    override suspend fun deleteMedicine(medicineId: String): Result<Unit> {

        fakeList = fakeList.copy(
            items = fakeList.items.filterNot { it.id == medicineId }
        )
        return Result.success(Unit)
    }
}