package com.oliviermarteaux.a055_rebonnte.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.oliviermarteaux.a055_rebonnte.domain.model.Aisle
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class AisleViewModel @Inject constructor() : ViewModel() {

    var aisle: Aisle by mutableStateOf(Aisle())
        private set

    fun selectAisle(selectedAisle: Aisle) {
        aisle = selectedAisle
    }
}