package com.oliviermarteaux.a055_rebonnte.domain.model

enum class MedicineChangeType(title: String){
    CREATION("Medicine entry creation"),
    STOCK_INCREASE("Medicine stock increase"),
    STOCK_DECREASE("Medicine stock decrease")
}