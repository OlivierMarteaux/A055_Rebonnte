package com.oliviermarteaux.a055_rebonnte.data.repository

import com.oliviermarteaux.a055_rebonnte.domain.model.Medicine
import com.oliviermarteaux.a055_rebonnte.ui.screen.MedicineSortOption
import kotlinx.coroutines.flow.Flow

interface MedicineRepository {

//    fun getMedicineSortedByDescTimestamp(): Flow<Result<List<Medicine>>>
    fun getMedicineSortedAndFilteredBy(query: String, medicineSortOption: MedicineSortOption): Flow<Result<List<Medicine>>>
    suspend fun addMedicine(medicine: Medicine): Result<Unit>
    suspend fun updateMedicine(medicine: Medicine): Result<Unit>
    suspend fun deleteMedicine(medicineId: String): Result<Unit>
}