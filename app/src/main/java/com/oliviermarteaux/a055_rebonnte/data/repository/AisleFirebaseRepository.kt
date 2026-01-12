package com.oliviermarteaux.a055_rebonnte.data.repository

import com.oliviermarteaux.a055_rebonnte.data.service.AisleApi
import com.oliviermarteaux.a055_rebonnte.domain.model.Aisle
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AisleFirebaseRepository @Inject constructor(
    private val aisleApi: AisleApi,
): AisleRepository {

    override fun getAislesSortedByDescTimestamp(): Flow<Result<List<Aisle>>> =
        aisleApi.getAislesSortedByDescTimestamp()

    override suspend fun addAisle(aisle: Aisle): Result<Unit> =
        aisleApi.addAisle(aisle)
}
