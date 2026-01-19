package com.oliviermarteaux.a055_rebonnte.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentSnapshot
import com.oliviermarteaux.a055_rebonnte.data.fake.fakeAisleList
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
    private var lastSnapshot: DocumentSnapshot? = null
    var isLastPage = false
        private set
    val medicineList = mutableStateListOf<Medicine>()
    var medicineListUiState: ListUiState<Medicine> by mutableStateOf(ListUiState.Loading)
        private set

//    var medicineList: List<Medicine> by mutableStateOf(emptyList())
//        private set

//    var filteredMedicineList: List<Medicine> by mutableStateOf(emptyList())
//        private set

    var currentSortOption: MedicineSortOption by mutableStateOf(MedicineSortOption.DESCENDING_TIMESTAMP)
        private set

    var queryFieldValue: TextFieldValue by mutableStateOf(TextFieldValue(""))
        private set

    fun loadFirstPage() {
        lastSnapshot = null
        isLastPage = false
        medicineList.clear()
        loadNextPage()
    }

    fun loadNextPage() {
        if (isLastPage) return

        viewModelScope.launch {
            medicineListUiState = ListUiState.Loading

            medicineRepository.getMedicinesFilteredSortedPaged(
                query = queryFieldValue.text.lowercase(),
                medicineSortOption = currentSortOption,
                pageSize = 9,
                lastSnapshot = lastSnapshot
            ).collect { result ->
                result.onSuccess { page ->
                    medicineList.addAll(page.items)
                    lastSnapshot = page.lastSnapshot
                    isLastPage = page.isLastPage
                    medicineListUiState =
                        if (medicineList.isEmpty()) ListUiState.Empty
                        else ListUiState.Success(medicineList)
                }.onFailure { e ->
                    medicineListUiState = ListUiState.Error(e)
                }
            }
        }
    }

    fun clearQuery() {
        queryFieldValue = TextFieldValue("")
        loadFirstPage()
//        getMedicineSortedAndFiltered()
//        filterMedicines(queryFieldValue)
    }

    fun filterMedicines(query: TextFieldValue) {
        queryFieldValue = query
        loadFirstPage()
//        getMedicineSortedAndFiltered()
//        filteredMedicineList = medicineList.filter { medicine ->
//            listOfNotNull(medicine.name, medicine.author?.firstname, medicine.author?.lastname)
//                .any { field -> field.contains(query.text, true) }
//        }.sortedWith ( currentSortOption.comparator )
    }

    fun sortMedicinesBy(sortOption: MedicineSortOption) {
        currentSortOption = sortOption
        loadFirstPage()
//        filteredMedicineList = filteredMedicineList.sortedWith(sortOption.comparator)
    }

//    fun loadMedicines() {
//        viewModelScope.launch {
//            medicineListUiState = ListUiState.Loading
////            delay(1500) // simulate network delay for Loading state evidence
//            medicineRepository.getMedicineSortedBy(currentSortOption).collect { result ->
//                result
//                    .onSuccess {
//                        medicineList = it
//                        filteredMedicineList = it
//                        medicineListUiState =
//                            if (medicineList.isEmpty()) ListUiState.Empty
//                            else ListUiState.Success(medicineList)
//                    }
//                    .onFailure { e ->
//                        medicineListUiState = ListUiState.Error(e)
//                    }
//            }
//        }
//    }

//    fun getMedicineSortedAndFiltered() {
//        viewModelScope.launch {
//            medicineListUiState = ListUiState.Loading
////            delay(1500) // simulate network delay for Loading state evidence
//            medicineRepository.getMedicineSortedAndFilteredBy(
//                query = queryFieldValue.text.lowercase(),
//                medicineSortOption = currentSortOption
//            ).collect { result ->
//                result
//                    .onSuccess {
//                        medicineList = it
//                        filteredMedicineList = it
//                        medicineListUiState =
//                            if (medicineList.isEmpty()) ListUiState.Empty
//                            else ListUiState.Success(medicineList)
//                    }
//                    .onFailure { e ->
//                        medicineListUiState = ListUiState.Error(e)
//                    }
//            }
//        }
//    }

    private fun signInTestUser(){
        viewModelScope.launch {
            userRepository.signIn(
                email = "fievel.farwest@example.com",
                password = "test123&",
            )
        }
    }

    fun populateFakeMedicineListForDemo(
        dataDispatcher: CoroutineDispatcher = Dispatchers.IO,
    ) {
        viewModelScope.launch(dataDispatcher) {
            fakeMedicineList.forEach {
                medicineRepository.addMedicine(it)
            }
        }
    }

    init {
        // throw RuntimeException("Test Crash") // Force a crash
        log.d("MedicineListViewModel: init")

        // Sign in the test user in case of test config
        if (TestConfig.isTest) signInTestUser()

        // Fetch medicines from the repository
        sortMedicinesBy(MedicineSortOption.DESCENDING_TIMESTAMP)
    }
}