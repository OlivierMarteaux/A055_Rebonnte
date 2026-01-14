package com.oliviermarteaux.a055_rebonnte.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewModelScope
import com.oliviermarteaux.a055_rebonnte.data.repository.MedicineRepository
import com.oliviermarteaux.a055_rebonnte.domain.model.Medicine
import com.oliviermarteaux.localshared.utils.TestConfig
import com.oliviermarteaux.shared.firebase.authentication.data.repository.UserRepository
import com.oliviermarteaux.shared.firebase.authentication.ui.AuthUserViewModel
import com.oliviermarteaux.shared.ui.ListUiState
import com.oliviermarteaux.shared.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedicineListViewModel @Inject constructor(
    private val medicineRepository: MedicineRepository,
    private val userRepository: UserRepository,
    log: Logger,
    isOnlineFlow: Flow<Boolean>
) : AuthUserViewModel(
    userRepository = userRepository,
    isOnlineFlow = isOnlineFlow,
    log = log
) {
    var medicineListUiState: ListUiState<Medicine> by mutableStateOf(ListUiState.Loading)
        private set

    var medicineList: List<Medicine> by mutableStateOf(emptyList())
        private set

    var filteredMedicineList: List<Medicine> by mutableStateOf(emptyList())
        private set

    var currentSortOption: MedicineSortOption? by mutableStateOf(null)
        private set

    var queryFieldValue: TextFieldValue by mutableStateOf(TextFieldValue(""))
        private set

    fun clearQuery() {
        queryFieldValue = TextFieldValue("")
        filterMedicines(queryFieldValue)
    }

    fun filterMedicines(query: TextFieldValue) {
        queryFieldValue = query
        filteredMedicineList = medicineList.filter { medicine ->
            listOfNotNull(medicine.name, medicine.author?.firstname, medicine.author?.lastname)
                .any { field -> field.contains(query.text, true) }
        }.sortedWith ( currentSortOption?.comparator?:compareBy { null } )
    }

    fun sortMedicinesBy(sortOption: MedicineSortOption) {
        currentSortOption = sortOption
        filteredMedicineList = filteredMedicineList.sortedWith(sortOption.comparator)
    }

    fun loadMedicines() {
        viewModelScope.launch {
            medicineListUiState = ListUiState.Loading
//            delay(1500) // simulate network delay for Loading state evidence
            medicineRepository.getMedicineSortedByDescTimestamp().collect { result ->
                result
                    .onSuccess {
                        medicineList = it
                        filteredMedicineList = it
                        medicineListUiState =
                            if (medicineList.isEmpty()) ListUiState.Empty
                            else ListUiState.Success(medicineList)
                    }
                    .onFailure { e ->
                        medicineListUiState = ListUiState.Error(e)
                    }
            }
        }
    }

    private fun signInTestUser(){
        viewModelScope.launch {
            userRepository.signIn(
                email = "fievel.farwest@example.com",
                password = "test123&",
            )
        }
    }

    init {
        // throw RuntimeException("Test Crash") // Force a crash
        log.d("MedicineListViewModel: init")

        // Sign in the test user in case of test config
        if (TestConfig.isTest) signInTestUser()

        // Fetch medicines from the repository
        loadMedicines()
    }
}