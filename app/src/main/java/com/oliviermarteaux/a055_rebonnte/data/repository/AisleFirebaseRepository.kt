package com.oliviermarteaux.a055_rebonnte.data.repository

import com.google.firebase.firestore.DocumentSnapshot
import com.oliviermarteaux.a055_rebonnte.data.service.AisleApi
import com.oliviermarteaux.a055_rebonnte.domain.model.Aisle
import com.oliviermarteaux.a055_rebonnte.domain.model.Medicine
import com.oliviermarteaux.a055_rebonnte.ui.screen.MedicineSortOption
import com.oliviermarteaux.a055_rebonnte.ui.screen.PagedList
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AisleFirebaseRepository @Inject constructor(
    private val aisleApi: AisleApi,
): AisleRepository {

    override fun getAislePaged(
        pageSize: Long,
        lastSnapshot: DocumentSnapshot?
    ): Flow<Result<PagedList<Aisle>>> =
        aisleApi.getAislePaged(
            pageSize = pageSize,
            lastSnapshot = lastSnapshot
        )

    override fun getAislesSortedByDescTimestamp(): Flow<Result<List<Aisle>>> =
        aisleApi.getAislesSortedByDescTimestamp()

    override suspend fun addAisle(aisle: Aisle): Result<Unit> =
        aisleApi.addAisle(aisle)
}
