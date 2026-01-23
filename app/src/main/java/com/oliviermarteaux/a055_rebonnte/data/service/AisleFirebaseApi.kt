package com.oliviermarteaux.a055_rebonnte.data.service

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.oliviermarteaux.a055_rebonnte.domain.model.Aisle
import com.oliviermarteaux.a055_rebonnte.ui.PagedList
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class AisleFirebaseApi: AisleApi {

    private val firestore = FirebaseFirestore.getInstance()
    private val aislesCollection = firestore.collection("aisles")

    // flow (push)
    override fun getAislePaged(
        pageSize: Long,
        lastSnapshot: DocumentSnapshot?
    ): Flow<Result<PagedList<Aisle>>> = flow {

        var queryRef = aislesCollection
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(pageSize)

        if (lastSnapshot != null) {
            queryRef = queryRef.startAfter(lastSnapshot)
        }

        val snapshot = queryRef.get().await()

        val aisleList = snapshot.documents.mapNotNull {
            it.toObject(Aisle::class.java)?.copy(id = it.id)
        }

        Log.d("OM_TAG", "AisleFirebaseApi: getAislePaged: AisleList.size = ${aisleList.size}")
        Log.d("OM_TAG", "AisleFirebaseApi: getAislePaged: pageSize = $pageSize")
        Log.d("OM_TAG", "AisleFirebaseApi: getAislePaged: isLastPage = ${aisleList.size < pageSize}")
        emit(
            Result.success(
                PagedList(
                    items = aisleList,
                    lastSnapshot = snapshot.documents.lastOrNull(),
                    isLastPage = aisleList.size < pageSize
                )
            )
        )
    }.catch { e ->
        Log.e("OM_TAG", "AisleFirebaseApi: getAislePaged: failed", e)
        emit(Result.failure(e))
    }
    
//    override fun getAislesSortedByDescTimestamp(): Flow<Result<List<Aisle>>> = callbackFlow {
//
//        // throw IllegalStateException("Forced exception for testing")
//
//        val listenerRegistration = aislesCollection
//            .orderBy("timestamp", Query.Direction.DESCENDING)
//            .addSnapshotListener { snapshot, error ->
//                when {
//                    error != null -> {
//                        Log.e("OM_TAG", "AisleFirebaseApi: getAislesSortedByDescTimestamp(): Firestore listener error: ${error.message}", error)
//                        trySend(Result.failure(error))
//                    }
//
//                    snapshot != null -> {
//                        val aisles = snapshot.documents.mapNotNull { doc ->
//                            doc.toObject(Aisle::class.java)?.copy(id = doc.id)
//                        }
//                        trySend(Result.success(aisles))
//                    }
//                }
//            }
//        awaitClose { listenerRegistration.remove() }
//    }.catch { e ->
//        // catches coroutine/flow cancellation or unexpected exceptions
//        Log.e("OM_TAG", "AisleFirebaseApi: getAislesSortedByDescTimestamp(): Flow exception: ${e.message}", e)
//        emit(Result.failure(e))
//    }

    override suspend fun addAisle(aisle: Aisle): Result<Unit> = runCatching {

        // throw IllegalStateException("Forced exception for testing")

        val docRef = aislesCollection.document() // generates ID locally
        val aisleId = docRef.id

        // Add aisle to Firestore aisles collection
        docRef.set(aisle.copy(id = aisleId)).await()
//        aislesCollection.add(aisle).await()

        Log.d("OM_TAG", "AisleFirebaseApi: addAisle: success")
        Unit

    }.onFailure { e ->
        Log.e("OM_TAG", "AisleFirebaseApi: addAisle: failed due to Exception: ${e.message}")
    }




}