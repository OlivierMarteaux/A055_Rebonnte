package com.oliviermarteaux.a055_rebonnte.data.service

import android.util.Log
import androidx.core.net.toUri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.oliviermarteaux.a055_rebonnte.domain.model.Aisle
import com.oliviermarteaux.shared.firebase.firestore.domain.model.Post
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.tasks.await

class AisleFirebaseApi: AisleApi {

    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val aislesCollection = firestore.collection("aisles")

    override fun getAislesSortedByDescTimestamp(): Flow<Result<List<Aisle>>> = callbackFlow {

        // throw IllegalStateException("Forced exception for testing")

        val listenerRegistration = aislesCollection
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                when {
                    error != null -> {
                        Log.e("OM_TAG", "AisleFirebaseApi: getAislesSortedByDescTimestamp(): Firestore listener error: ${error.message}", error)
                        trySend(Result.failure(error))
                    }

                    snapshot != null -> {
                        val aisles = snapshot.documents.mapNotNull { doc ->
                            doc.toObject(Aisle::class.java)?.copy(id = doc.id)
                        }
                        trySend(Result.success(aisles))
                    }
                }
            }
        awaitClose { listenerRegistration.remove() }
    }.catch { e ->
        // catches coroutine/flow cancellation or unexpected exceptions
        Log.e("OM_TAG", "AisleFirebaseApi: getAislesSortedByDescTimestamp(): Flow exception: ${e.message}", e)
        emit(Result.failure(e))
    }

    override suspend fun addAisle(aisle: Aisle): Result<Unit> = runCatching {

        // throw IllegalStateException("Forced exception for testing")

        // Add aisle to Firestore aisles collection
        aislesCollection.add(aisle).await()
        Log.d("OM_TAG", "AisleFirebaseApi: addAisle: success")
        Unit

    }.onFailure { e ->
        Log.e("OM_TAG", "AisleFirebaseApi: addAisle: failed due to Exception: ${e.message}")
    }
}