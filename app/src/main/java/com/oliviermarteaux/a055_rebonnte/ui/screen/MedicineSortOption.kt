package com.oliviermarteaux.a055_rebonnte.ui.screen

import com.google.firebase.firestore.Query
import com.oliviermarteaux.a055_rebonnte.domain.model.Medicine

enum class SortOrder {
    ASCENDING, DESCENDING
}

enum class MedicineSortOption(
    val field: String,
    val direction: Query.Direction,
    val comparator: Comparator<Medicine>
) {
    DESCENDING_TIMESTAMP("timestamp", Query.Direction.DESCENDING, compareByDescending { it.timestamp }),
    ASCENDING_NAME("name", Query.Direction.ASCENDING,compareBy { it.name }),
    ASCENDING_STOCK("stock", Query.Direction.ASCENDING,compareBy { it.stock }),
    DESCENDING_STOCK("stock", Query.Direction.DESCENDING,compareByDescending { it.stock })
}