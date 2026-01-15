package com.oliviermarteaux.a055_rebonnte.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.oliviermarteaux.a055_rebonnte.domain.model.Aisle
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AisleViewModel @Inject constructor() : ViewModel() {

    var aisle: Aisle by mutableStateOf(Aisle())
        private set

    fun selectAisle(selectedAisle: Aisle) {
        aisle = selectedAisle
    }
}