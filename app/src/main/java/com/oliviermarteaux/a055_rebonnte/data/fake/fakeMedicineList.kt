package com.oliviermarteaux.a055_rebonnte.data.fake

import com.oliviermarteaux.a055_rebonnte.domain.model.Medicine
import com.oliviermarteaux.a055_rebonnte.domain.model.MedicineChange

val fakeMedicineList = drugNameList.mapIndexed { index, name ->
    val author = fakeUserList[index % fakeUserList.size]
    val aisle = fakeAisleList[index % fakeAisleList.size]
    val stock = (index % 120) + 10

    Medicine(
        name = name,
        aisle = aisle,
        stock = stock,
        author = author,
        changeRecord = listOf(
            MedicineChange(
                author = author,
                newStock = stock
            )
        ),
        nameLowerCase = name.lowercase()
    )
}