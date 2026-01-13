package com.oliviermarteaux.a055_rebonnte.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.oliviermarteaux.a055_rebonnte.domain.model.Aisle
import com.oliviermarteaux.a055_rebonnte.domain.model.Medicine
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MedicineViewModel @Inject constructor() : ViewModel() {

    var medicine: Medicine by mutableStateOf(Medicine())
        private set

    fun selectMedicine(selectedMedicine: Medicine) {
        medicine = selectedMedicine
    }
}