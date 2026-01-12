package com.oliviermarteaux.a055_rebonnte.data.service

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.oliviermarteaux.a055_rebonnte.domain.model.Medicine
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.tasks.await

class MedicineFirebaseApi: MedicineApi {

    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val medicinesCollection = firestore.collection("medicines")

    override fun getMedicineSortedByDescTimestamp(): Flow<Result<List<Medicine>>> = callbackFlow {
//        throw IllegalStateException("Forced exception for testing")
        val listenerRegistration = medicinesCollection
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                when {
                    error != null -> {
                        Log.e(
                            "OM_TAG",
                            "MedicineFirebaseApi: getMedicinesSortedByDescTimestamp(): Firestore listener error: ${error.message}",
                            error
                        )
                        trySend(Result.failure(error))
                    }

                    snapshot != null -> {
                        val medicines = snapshot.documents.mapNotNull { doc ->
                            doc.toObject(Medicine::class.java)?.copy(id = doc.id)
                        }
                        trySend(Result.success(medicines))
                    }
                }
            }
        awaitClose { listenerRegistration.remove() }
    }.catch { e ->
        // catches coroutine/flow cancellation or unexpected exceptions
        Log.e(
            "OM_TAG",
            "MedicineFirebaseApi: getMedicinesSortedByDescTimestamp(): Flow exception: ${e.message}",
            e
        )
        emit(Result.failure(e))
    }

    override suspend fun addMedicine(medicine: Medicine): Result<Unit> = runCatching {

        // throw IllegalStateException("Forced exception for testing")

        // Add medicine to Firestore medicines collection
        medicinesCollection.add(medicine).await()
        Log.d("OM_TAG", "MedicineFirebaseApi: addMedicine: success")
        Unit

    }.onFailure { e ->
        Log.e(
            "OM_TAG",
            "MedicineFirebaseApi: addMedicine: failed due to Exception: ${e.message}",
            e
        )
    }

    override suspend fun updateMedicine(medicine: Medicine): Result<Unit> = runCatching {

        require(medicine.id.isNotBlank()) { "Medicine ID must not be blank" }

        firestore
            .collection("medicines")
            .document(medicine.id)
            .set(medicine, SetOptions.merge())
            .await()

        Unit
    }.onFailure { e ->
        Log.e(
            "OM_TAG",
            "MedicineFirebaseApi: updateMedicine: failed due to Exception",
            e
        )
    }
}