package com.oliviermarteaux.a055_rebonnte.ui.screen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentSnapshot
import com.oliviermarteaux.a055_rebonnte.data.fake.fakeMedicineList
import com.oliviermarteaux.a055_rebonnte.data.repository.MedicineRepository
import com.oliviermarteaux.a055_rebonnte.domain.model.Medicine
import com.oliviermarteaux.localshared.utils.TestConfig
import com.oliviermarteaux.shared.firebase.authentication.data.repository.UserRepository
import com.oliviermarteaux.shared.firebase.authentication.ui.AuthUserViewModel
import com.oliviermarteaux.shared.ui.ListUiState
import com.oliviermarteaux.shared.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
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
    val medicineList = mutableStateListOf<Medicine>()
    var medicineListUiState: ListUiState<Medicine> by mutableStateOf(ListUiState.Loading)
        private set

    //_ ############################################################################################
    //_ Searching
    //_ ############################################################################################
    var queryFieldValue: TextFieldValue by mutableStateOf(TextFieldValue(""))
        private set
    fun clearQuery() {
        queryFieldValue = TextFieldValue("")
        loadFirstPage()
    }
    fun filterMedicines(query: TextFieldValue) {
        queryFieldValue = query
        loadFirstPage()
    }

    //_ ############################################################################################
    //_ Sorting
    //_ ############################################################################################
    var currentSortOption: MedicineSortOption by mutableStateOf(MedicineSortOption.DESCENDING_TIMESTAMP)
        private set

    fun sortMedicinesBy(sortOption: MedicineSortOption) {
        currentSortOption = sortOption
        loadFirstPage()
    }

    //_ ############################################################################################
    //_ List pageing
    //_ ############################################################################################
    private var lastSnapshot: DocumentSnapshot? = null
    var isLastPage by mutableStateOf(false)
        private set
    var isLoading = false
        private set

    fun loadFirstPage() {
        lastSnapshot = null
        isLastPage = false
        medicineList.clear()
        loadNextPage()
    }

    fun loadNextPage() {
        Log.d("OM_TAG","MedicineListViewModel::loadNextPage: isLastPage = $isLastPage")
        Log.d("OM_TAG","MedicineListViewModel::loadNextPage: isLoading = $isLoading")
        Log.d("OM_TAG","MedicineListViewModel::loadNextPage: return = ${(isLastPage || isLoading)}")
        if (isLastPage || isLoading) return

        viewModelScope.launch {
            isLoading = true

            medicineRepository.getMedicinesFilteredSortedPaged(
                query = queryFieldValue.text.lowercase(),
                medicineSortOption = currentSortOption,
                pageSize = 9,
                lastSnapshot = lastSnapshot
            ).collect { result ->
                result.onSuccess { page ->
                    val newItems = page.items.filter { it.id !in medicineList.map { m -> m.id } }
                    medicineList.addAll(newItems)
                    lastSnapshot = page.lastSnapshot
                    isLastPage = page.isLastPage
                    medicineListUiState =
                        if (medicineList.isEmpty()) ListUiState.Empty
                        else ListUiState.Success(medicineList)
                }.onFailure { e ->
                    medicineListUiState = ListUiState.Error(e)
                }
            }
            isLoading = false
        }
    }

    //_ ############################################################################################
    //_ Testing
    //_ ############################################################################################
    private fun signInTestUser(){
        viewModelScope.launch {
            userRepository.signIn(
                email = "fievel.farwest@example.com",
                password = "test123&",
            )
        }
    }

    //_ ############################################################################################
    //_ Pre-populating
    //_ ############################################################################################
    fun populateFakeMedicineListForDemo(
        dataDispatcher: CoroutineDispatcher = Dispatchers.IO,
    ) {
        viewModelScope.launch(dataDispatcher) {
            fakeMedicineList.forEach {
                medicineRepository.addMedicine(it)
            }
        }
    }

    //_ ############################################################################################
    //_ Init
    //_ ############################################################################################
    init {
        // throw RuntimeException("Test Crash") // Force a crash
        log.d("MedicineListViewModel: init")

        // Sign in the test user in case of test config
        if (TestConfig.isTest) signInTestUser()

        // Fetch medicines from the repository
        sortMedicinesBy(MedicineSortOption.DESCENDING_TIMESTAMP)
    }
}