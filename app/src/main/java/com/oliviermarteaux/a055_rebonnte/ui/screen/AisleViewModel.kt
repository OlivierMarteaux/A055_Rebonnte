package com.oliviermarteaux.a055_rebonnte.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.oliviermarteaux.a055_rebonnte.data.repository.AisleRepository
import com.oliviermarteaux.a055_rebonnte.domain.model.Aisle
import com.oliviermarteaux.shared.firebase.authentication.data.repository.UserRepository
import com.oliviermarteaux.shared.firebase.authentication.ui.AuthUserViewModel
import com.oliviermarteaux.shared.ui.UiState
import com.oliviermarteaux.shared.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.time.ExperimentalTime

@HiltViewModel
class AisleViewModel @Inject constructor(
    private val aisleRepository: AisleRepository,
    userRepository: UserRepository,
    isOnlineFlow: Flow<Boolean>,
    log: Logger
) : AuthUserViewModel(
    userRepository = userRepository,
    isOnlineFlow = isOnlineFlow,
    log = log
) {

    var aisle: Aisle by mutableStateOf(Aisle())
        private set

    var addAisleUiState: UiState<Unit> by mutableStateOf(UiState.Idle)
        private set

    fun selectAisle(selectedAisle: Aisle) {
        aisle = selectedAisle
    }

    fun updateAisleName(name: String) { aisle = aisle.copy(name = name) }

    /**
     * Attempts to add the current aisle to the repository after setting the author.
     */
    @OptIn(ExperimentalTime::class)
    fun addAisle(
        dataDispatcher: CoroutineDispatcher = Dispatchers.IO,
        layoutDispatcher: CoroutineDispatcher = Dispatchers.Main,
        onResult: () -> Unit
    ) {
        addAisleUiState = UiState.Loading
        if(!isOnline) {
            showNetworkErrorToast()
            addAisleUiState = UiState.Idle // 🟢 reset state since we're not adding the post
            return
        }
        //_ add the aisle to the repository
        checkUserState(
            onUserLogged = { user ->
                viewModelScope.launch(dataDispatcher) {
                    //      delay(3000) // simulate network delay for Loading state evidence
                    aisleRepository.addAisle(aisle.copy(author = user)).fold(
                        onSuccess = {
                            addAisleUiState = UiState.Success(Unit)
                            withContext(layoutDispatcher) { onResult() }
                                    },
                        onFailure = {
                            showUnknownErrorToast()
                            addAisleUiState = UiState.Idle
                        }
                    )
                }
            },
            onNoUserLogged = {
                showAuthErrorToast()
            }
        )
    }
}