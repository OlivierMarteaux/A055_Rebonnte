package com.oliviermarteaux.a055_rebonnte.ui.screen.addOrEditMedicine

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.oliviermarteaux.a055_rebonnte.data.repository.MedicineRepository
import com.oliviermarteaux.a055_rebonnte.domain.model.Aisle
import com.oliviermarteaux.a055_rebonnte.domain.model.Medicine
import com.oliviermarteaux.a055_rebonnte.domain.model.MedicineChange
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

@HiltViewModel
class AddOrEditMedicineViewModel @Inject constructor(
    private val medicineRepository: MedicineRepository,
    userRepository: UserRepository,
    isOnlineFlow: Flow<Boolean>,
    log: Logger
) : AuthUserViewModel(
    userRepository = userRepository,
    isOnlineFlow = isOnlineFlow,
    log = log
) {
    var addOrEditMedicineUiState: UiState<Unit> by mutableStateOf(UiState.Idle)
        private set

    var medicine: Medicine by mutableStateOf(Medicine())
        private set

    fun updateMedicineName(name: String) { medicine = medicine.copy(name = name) }
    fun updateMedicineAisle(aisle: Aisle) { medicine = medicine.copy(aisle = aisle) }
    fun updateMedicineStock(stock: Int) { medicine = medicine.copy(stock = stock) }

    /**
     * Attempts to add the current medicine to the repository after setting the author.
     */
    fun addMedicine(
        stock: Int,
        dataDispatcher: CoroutineDispatcher = Dispatchers.IO,
        layoutDispatcher: CoroutineDispatcher = Dispatchers.Main,
        onResult: () -> Unit
    ) {
        addOrEditMedicineUiState = UiState.Loading
        if(!isOnline) {
            showNetworkErrorToast()
            addOrEditMedicineUiState = UiState.Idle // 🟢 reset state since we're not adding the medicine
            return
        }
        //_ add the medicine to the repository
        checkUserState(
            onUserLogged = { user ->
                viewModelScope.launch(dataDispatcher) {
                    //      delay(3000) // simulate network delay for Loading state evidence
                    medicineRepository.addMedicine(medicine.copy(
                        author = user,
                        changeRecord = listOf(
                            MedicineChange(
                                title =  "Medicine entry creation",
                                newStock = stock
                            )
                        )
                    )).fold(
                        onSuccess = { withContext(layoutDispatcher) { onResult() } },
                        onFailure = { showUnknownErrorToast() }
                    )
                    addOrEditMedicineUiState = UiState.Idle
                }
            },
            onNoUserLogged = {
                showAuthErrorToast()
            }
        )
    }
}