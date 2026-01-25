package com.oliviermarteaux.a055_rebonnte.ui.viewModel

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
import com.oliviermarteaux.a055_rebonnte.domain.model.Aisle
import com.oliviermarteaux.a055_rebonnte.domain.model.Medicine
import com.oliviermarteaux.a055_rebonnte.ui.MedicineSortOption
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
import kotlin.math.min

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
    //_ Loading whole list
    //_ ############################################################################################

    fun getAllMedicineByDescendingTimestamp(){
        queryFieldValue = TextFieldValue("")
        currentSortOption = MedicineSortOption.DESCENDING_TIMESTAMP
        aisleId = ""
        loadFirstPage()
    }

    //_ ############################################################################################
    //_ Searching
    //_ ############################################################################################
    var queryFieldValue: TextFieldValue by mutableStateOf(TextFieldValue(""))
        private set

    private var aisleId: String = ""
    fun clearQuery() {
        queryFieldValue = TextFieldValue("")
        loadFirstPage()
    }
    fun filterMedicineByName(query: TextFieldValue) {
        queryFieldValue = query
        loadFirstPage()
    }
    fun filterMedicineByAisleId(selectedAisleId: String) {
        aisleId = selectedAisleId
        Log.d("OM_TAG", "MedicineListViewModel::filterMedicineByAisleId: selected aisle Id = $aisleId")
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
    //_ List paging
    //_ ############################################################################################
    private var lastSnapshot: DocumentSnapshot? = null
    var isLastPage by mutableStateOf(false)
        private set
//    var isLoading = false
//        private set

    private fun loadFirstPage() {
        Log.d("OM_TAG","MedicineListViewModel::loadFirstPage")
        lastSnapshot = null
        isLastPage = false
        medicineList.clear()
        loadNextPage()
    }

    fun loadNextPage() {
        Log.d("OM_TAG","MedicineListViewModel::loadNextPage: isLastPage = $isLastPage")
//        Log.d("OM_TAG","MedicineListViewModel::loadNextPage: isLoading = $isLoading")
//        Log.d("OM_TAG","MedicineListViewModel::loadNextPage: return = ${(isLastPage || isLoading)}")
        if (isLastPage/* || isLoading*/) return

        Log.d("OM_TAG", "MedicineListViewModel::loadNextPage: query = ${queryFieldValue.text.lowercase()}")

        viewModelScope.launch {
//            isLoading = true

            medicineRepository.getMedicinesFilteredSortedPaged(
                query = queryFieldValue.text.lowercase(),
                aisleId = aisleId,
                medicineSortOption = currentSortOption,
                pageSize = 9,
                lastSnapshot = lastSnapshot
            ).collect { result ->
                result.onSuccess { page ->
                    val newItems = page.items.filter { it.id !in medicineList.map { m -> m.id } }
//                    val newItems = page.items
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
//            isLoading = false
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
        aisleList: List<Aisle>,
        dataDispatcher: CoroutineDispatcher = Dispatchers.IO,
    ) {
        viewModelScope.launch(dataDispatcher) {
            fakeMedicineList.forEach {
                medicineRepository.addMedicine(
                    it.copy(
                        aisle = aisleList.random()
                    )
                )
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
    }
}