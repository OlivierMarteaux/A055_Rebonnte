package com.oliviermarteaux.a055_rebonnte.ui.screen.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentSnapshot
import com.oliviermarteaux.a055_rebonnte.data.fake.fakeAisleList
import com.oliviermarteaux.a055_rebonnte.data.fake.fakeMedicineList
import com.oliviermarteaux.a055_rebonnte.data.repository.AisleRepository
import com.oliviermarteaux.a055_rebonnte.domain.model.Aisle
import com.oliviermarteaux.a055_rebonnte.domain.model.Medicine
import com.oliviermarteaux.a055_rebonnte.domain.model.MedicineChange
import com.oliviermarteaux.a055_rebonnte.domain.model.MedicineChangeType
import com.oliviermarteaux.localshared.utils.TestConfig
import com.oliviermarteaux.shared.firebase.authentication.data.repository.UserRepository
import com.oliviermarteaux.shared.firebase.authentication.ui.AuthUserViewModel
import com.oliviermarteaux.shared.ui.ListUiState
import com.oliviermarteaux.shared.ui.UiState
import com.oliviermarteaux.shared.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val aisleRepository: AisleRepository,
    private val userRepository: UserRepository,
    log: Logger,
    isOnlineFlow: Flow<Boolean>
) : AuthUserViewModel(
    userRepository = userRepository,
    isOnlineFlow = isOnlineFlow,
    log = log,
) {
    var homeUiState: ListUiState<Aisle> by mutableStateOf(ListUiState.Loading)
        private set
    val aisleList = mutableStateListOf<Aisle>()

    //_ ############################################################################################
    //_ List paging
    //_ ############################################################################################
    private var lastSnapshot: DocumentSnapshot? = null
    var isLastPage by mutableStateOf(false)
        private set
    var isLoading = false
        private set

    fun loadFirstPage() {
        Log.d("OM_TAG","HomeViewModel::loadFirstPage")
        lastSnapshot = null
        isLastPage = false
        aisleList.clear()
        loadNextPage()
    }

    fun loadNextPage() {
        Log.d("OM_TAG","HomeViewModel::loadNextPage: isLastPage = $isLastPage")
        Log.d("OM_TAG","HomeViewModel::loadNextPage: isLoading = $isLoading")
        Log.d("OM_TAG","HomeViewModel::loadNextPage: return = ${(isLastPage || isLoading)}")
        if (isLastPage || isLoading) return

        viewModelScope.launch {
            isLoading = true

            aisleRepository.getAislePaged(
                pageSize = 9,
                lastSnapshot = lastSnapshot
            ).collect { result ->
                result.onSuccess { page ->
                    val newItems = page.items.filter { it.id !in aisleList.map { m -> m.id } }
                    aisleList.addAll(newItems)
                    lastSnapshot = page.lastSnapshot
                    isLastPage = page.isLastPage
                    homeUiState =
                        if (aisleList.isEmpty()) ListUiState.Empty
                        else ListUiState.Success(aisleList)
                }.onFailure { e ->
                    homeUiState = ListUiState.Error(e)
                }
            }
            isLoading = false
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

    fun populateFakeAisleListForDemo(
        dataDispatcher: CoroutineDispatcher = Dispatchers.IO,
    ) {
        viewModelScope.launch(dataDispatcher) {
            fakeAisleList.forEach {
                aisleRepository.addAisle(it)
            }
        }
    }

    init {
        // throw RuntimeException("Test Crash") // Force a crash
        log.d("HomeViewModel: init")

        // Sign in the test user in case of test config
        if (TestConfig.isTest) signInTestUser()

        // Fetch posts from the repository
        loadFirstPage()
    }
}