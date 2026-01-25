package com.oliviermarteaux.a055_rebonnte.ui.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentSnapshot
import com.oliviermarteaux.a055_rebonnte.data.repository.AisleRepository
import com.oliviermarteaux.a055_rebonnte.domain.model.Aisle
import com.oliviermarteaux.shared.firebase.authentication.data.repository.UserRepository
import com.oliviermarteaux.shared.firebase.authentication.ui.AuthUserViewModel
import com.oliviermarteaux.shared.ui.ListUiState
import com.oliviermarteaux.shared.utils.Logger
import com.oliviermarteaux.shared.utils.TestConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AisleListViewModel @Inject constructor(
    private val aisleRepository: AisleRepository,
    private val userRepository: UserRepository,
    private val log: Logger,
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

    fun getAllAisle(){
        log.d("AisleListViewModel::getAllAisle")
        lastSnapshot = null
        isLastPage = false
        aisleList.clear()
        loadNextPage(pageSize = 100)
    }

    fun loadFirstPage() {
        log.d("AisleListViewModel::loadFirstPage")
        lastSnapshot = null
        isLastPage = false
        aisleList.clear()
        loadNextPage()
    }

    fun loadNextPage(pageSize: Long = 9) {
        log.d("AisleListViewModel::loadNextPage: isLastPage = $isLastPage")
        log.d("AisleListViewModel::loadNextPage: isLoading = $isLoading")
        log.d("AisleListViewModel::loadNextPage: return = ${(isLastPage || isLoading)}")
        if (isLastPage || isLoading) return

        viewModelScope.launch {
            isLoading = true

            aisleRepository.getAislePaged(
                pageSize = pageSize,
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

//    fun populateFakeAisleListForDemo(
//        dataDispatcher: CoroutineDispatcher = Dispatchers.IO,
//    ) {
//        viewModelScope.launch(dataDispatcher) {
//            fakeAisleList.forEach {
//                aisleRepository.addAisle(it)
//            }
//        }
//    }

    init {
        // throw RuntimeException("Test Crash") // Force a crash
        log.d("AisleListViewModel: init")

        // Sign in the test user in case of test config
        if (TestConfig.isTest) {
            signInTestUser()
            loadFirstPage()
        }

        // fixed: do not load in init as it leads to auth error when instantiated on SplashScreen
//        loadFirstPage()
    }
}