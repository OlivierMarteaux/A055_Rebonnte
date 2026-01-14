package com.oliviermarteaux.a055_rebonnte.domain.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.oliviermarteaux.a055_rebonnte.R
import com.oliviermarteaux.shared.extensions.toDate
import com.oliviermarteaux.shared.extensions.toLocalDateString
import com.oliviermarteaux.shared.extensions.toLocalTimeString
import com.oliviermarteaux.shared.firebase.authentication.domain.model.User
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.util.Date
import java.util.UUID

data class MedicineChange(

    val id: String = UUID.randomUUID().toString(),
    val type: MedicineChangeType = MedicineChangeType.CREATION,
    val timestamp: Long = System.currentTimeMillis(),
    val author: User? = User(),
    val date: Date? = LocalDate.now().toDate(),
    val time: Date? = LocalDate.now().toDate(),
    val previousStock: Int = 0,
    val newStock: Int = 0

): Serializable {

    val localeDate: LocalDate?
        get() = date?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate()

    val localeDateString: String
        get() = localeDate?.toLocalDateString() ?: ""

    val localeTime: LocalTime?
        get() = time?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalTime()

    val localeTimeString: String
        get() = localeTime?.toLocalTimeString() ?: ""

    @Composable
    fun getTitle(): String =
            when (type) {
                MedicineChangeType.CREATION -> stringResource(
                    R.string.medicine_entry_creation,
                    localeDateString
                )

                MedicineChangeType.STOCK_DECREASE -> stringResource(
                    R.string.medicine_stock_decreased,
                    localeDateString
                )

                MedicineChangeType.STOCK_INCREASE -> stringResource(
                    R.string.medicine_stock_increased,
                    localeDateString
                )
            }

    @Composable
    fun getDescription(): String =
            when (type) {
                MedicineChangeType.CREATION -> stringResource(
                    R.string.initial_stock_set_to_by,
                    newStock,
                    author?.getComputedFullName() ?: ""
                )

                MedicineChangeType.STOCK_DECREASE -> stringResource(
                    R.string.stock_decreased_from_to_by,
                    previousStock,
                    newStock,
                    author?.getComputedFullName() ?: ""
                )

                MedicineChangeType.STOCK_INCREASE -> stringResource(
                    R.string.stock_increased_from_to_by,
                    previousStock,
                    newStock,
                    author?.getComputedFullName() ?: ""
                )
            }
}