package com.oliviermarteaux.a055_rebonnte.data.repository

import com.oliviermarteaux.a055_rebonnte.data.service.MedicineApi
import com.oliviermarteaux.a055_rebonnte.domain.model.Medicine
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MedicineFirebaseRepository @Inject constructor(
    private val medicineApi: MedicineApi,
): MedicineRepository {

    override fun getMedicineSortedByDescTimestamp(): Flow<Result<List<Medicine>>> =
        medicineApi.getMedicineSortedByDescTimestamp()

    override suspend fun addMedicine(medicine: Medicine): Result<Unit> =
        medicineApi.addMedicine(medicine)

    override suspend fun updateMedicine(medicine: Medicine): Result<Unit> =
        medicineApi.updateMedicine(medicine)
}