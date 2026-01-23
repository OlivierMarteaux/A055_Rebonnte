package com.oliviermarteaux.a055_rebonnte.fake

import com.google.firebase.firestore.DocumentSnapshot
import com.oliviermarteaux.a055_rebonnte.data.fake.fakeAisleList
import com.oliviermarteaux.a055_rebonnte.data.repository.AisleRepository
import com.oliviermarteaux.a055_rebonnte.domain.model.Aisle
import com.oliviermarteaux.a055_rebonnte.ui.PagedList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class AisleFakeRepository: AisleRepository {

    var fakeList: PagedList<Aisle> = PagedList(
        items = fakeAisleList,
        lastSnapshot = null,
        isLastPage = false
    )

    override fun getAislePaged(
        pageSize: Long,
        lastSnapshot: DocumentSnapshot?
    ): Flow<Result<PagedList<Aisle>>> = flowOf(Result.success(fakeList))

    override suspend fun addAisle(aisle: Aisle): Result<Unit> {

        val newFakeList = listOf(aisle) + fakeAisleList
        fakeList = fakeList.copy(items = newFakeList)
        return Result.success(Unit)
    }
}