package com.oliviermarteaux.a055_rebonnte.ui.screen.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.oliviermarteaux.a055_rebonnte.data.repository.AisleRepository
import com.oliviermarteaux.a055_rebonnte.domain.model.Aisle
import com.oliviermarteaux.localshared.utils.TestConfig
import com.oliviermarteaux.shared.firebase.authentication.data.repository.UserRepository
import com.oliviermarteaux.shared.firebase.authentication.ui.AuthUserViewModel
import com.oliviermarteaux.shared.ui.ListUiState
import com.oliviermarteaux.shared.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for managing data and events related to the Home screen.
 * This ViewModel retrieves aisles from the AisleRepository and exposes them as a Flow<List<Aisle>>,
 * allowing UI components to observe and react to changes in the aisles data.
 */
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
    /**
     * The UI state for the home feed.
     */
    var homeUiState: ListUiState<Aisle> by mutableStateOf(ListUiState.Loading)
        private set

    var aisleList: List<Aisle> by mutableStateOf(emptyList())
        private set


    /**
     * Loads the posts from the repository.
     */
    fun loadAisles() {
        viewModelScope.launch {
            homeUiState = ListUiState.Loading
//            delay(1500) // simulate network delay for Loading state evidence
            aisleRepository.getAislesSortedByDescTimestamp().collect { result ->
                result
                    .onSuccess {
                        aisleList = it
                        homeUiState =
                            if (aisleList.isEmpty()) ListUiState.Empty
                            else ListUiState.Success(aisleList)
                    }
                    .onFailure { e ->
                        homeUiState = ListUiState.Error(e)
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
        log.d("HomeFeedViewModel: init")

        // Sign in the test user in case of test config
        if (TestConfig.isTest) signInTestUser()

        // Fetch posts from the repository
        loadAisles()
    }
}