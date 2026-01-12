package com.oliviermarteaux.a055_rebonnte.ui.screen.account

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.oliviermarteaux.shared.datastore.NotificationPreferencesRepository
import com.oliviermarteaux.shared.firebase.authentication.data.repository.UserRepository
import com.oliviermarteaux.shared.firebase.authentication.domain.model.User
import com.oliviermarteaux.shared.firebase.authentication.ui.AuthUserViewModel
import com.oliviermarteaux.shared.ui.UiState
import com.oliviermarteaux.shared.utils.CoroutineDispatcherProvider
import com.oliviermarteaux.shared.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val notificationPreferencesRepository: NotificationPreferencesRepository,
    private val log: Logger,
    private val isOnlineFlow: Flow<Boolean>,
    private val dispatchers: CoroutineDispatcherProvider, // used in UnitTests
) : AuthUserViewModel(
    userRepository = userRepository,
    isOnlineFlow = isOnlineFlow,
    log = log,
) {
    var user: User by mutableStateOf(User())
        private set
    var notificationState: Boolean by mutableStateOf(true)
        private set

    var userUiState: UiState<User> by mutableStateOf(UiState.Loading)

    /**
     * Toggles the notification preference.
     *
     */
    fun toggleNotifications() {
        viewModelScope.launch {
            notificationState = !notificationState
            notificationPreferencesRepository.saveNotificationPreference(notificationState)
            log.d("AccountViewModel: toggleNotifications(): $notificationState")
        }
    }

    /**
     * Gets the post from the repository.
     */
    private fun getNotifState(){
        viewModelScope.launch {
            notificationPreferencesRepository.isNotifEnabled.collect { result ->
                notificationState = result
                log.d("AccountViewModel: getNotifState(): notificationState = $notificationState")
            }
        }
    }

    /**
     * Gets the current user logged in Firebase.
     */
    private fun getCurrentUser(){
        viewModelScope.launch {
//            delay(1500) // for test
            userRepository.userAuthState
                .collect { currentUser ->
                    if (currentUser != null) {
                        userUiState = UiState.Success(currentUser)
                        this@AccountViewModel.user = currentUser
                        log.d("AccountViewModel: user updated to ${currentUser.email}")
                        log.d("AccountViewModel: userPhotoUrl = ${currentUser.photoUrl}")
                    } else {
                        userUiState = UiState.Error(Throwable("No user logged in"))
                        log.d("AccountViewModel: no user logged in")
                        log.d("AccountViewModel: userPhotoUrl = ${user.photoUrl}")
                    }
                }
        }
    }

    init {
        userUiState = UiState.Loading
        getCurrentUser()
        getNotifState()
    }
}