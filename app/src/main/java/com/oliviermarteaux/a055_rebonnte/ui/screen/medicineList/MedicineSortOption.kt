package com.oliviermarteaux.a055_rebonnte.ui.screen.medicineList

import com.oliviermarteaux.a055_rebonnte.domain.model.Medicine

enum class MedicineSortOption(val comparator: Comparator<Medicine>) {
    NONE({ _, _ -> 0 }), // neutral comparator
    NAME(compareBy { it.name }),
    ASCENDING_STOCK(compareBy { it.stock }),
    DESCENDING_STOCK(compareByDescending { it.stock })
}