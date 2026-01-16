package com.oliviermarteaux.a055_rebonnte.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.oliviermarteaux.a055_rebonnte.data.repository.MedicineRepository
import com.oliviermarteaux.a055_rebonnte.domain.model.Aisle
import com.oliviermarteaux.a055_rebonnte.domain.model.Medicine
import com.oliviermarteaux.a055_rebonnte.domain.model.MedicineChange
import com.oliviermarteaux.a055_rebonnte.domain.model.MedicineChangeType
import com.oliviermarteaux.shared.firebase.authentication.data.repository.UserRepository
import com.oliviermarteaux.shared.firebase.authentication.ui.AuthUserViewModel
import com.oliviermarteaux.shared.ui.UiState
import com.oliviermarteaux.shared.ui.showToastFlag
import com.oliviermarteaux.shared.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

enum class CrudAction {
    NONE, GET, ADD, UPDATE, DELETE
}

@HiltViewModel
class MedicineViewModel @Inject constructor(
    private val medicineRepository: MedicineRepository,
    userRepository: UserRepository,
    isOnlineFlow: Flow<Boolean>,
    private val log: Logger
) : AuthUserViewModel(
    userRepository = userRepository,
    isOnlineFlow = isOnlineFlow,
    log = log
) {
    var medicine: Medicine by mutableStateOf(Medicine())
        private set
    var sourceMedicine: Medicine by mutableStateOf(Medicine())
        private set
    var medicineCrudAction: CrudAction by mutableStateOf(CrudAction.NONE)
        private set
    var addOrEditMedicineUiState: UiState<Unit> by mutableStateOf(UiState.Idle)
        private set
    fun resetAddOrEditMedicineUiState() {
        viewModelScope.launch {
            delay(3000)
            addOrEditMedicineUiState = UiState.Idle
        }
    }
    fun resetMedicineCrudAction() {
        viewModelScope.launch {
            delay(3000)
            medicineCrudAction = CrudAction.NONE
        }
    }
    fun switchToMedicineCreationMode(){
        log.d("MedicineViewModel::switchToMedicineCreationMode")
        medicineCrudAction = CrudAction.ADD
    }
    fun switchToMedicineEditionMode(){
        log.d("MedicineViewModel::switchToMedicineEditionMode")
        medicineCrudAction = CrudAction.UPDATE
    }
    fun switchToMedicineDeletionMode(){
        log.d("MedicineViewModel::switchToMedicineDeletionMode")
        medicineCrudAction = CrudAction.DELETE
    }

    fun selectMedicine(selectedMedicine: Medicine) {
        sourceMedicine = selectedMedicine
        medicine = selectedMedicine
    }
    fun updateMedicineName(name: String) { medicine = medicine.copy(name = name) }
    fun updateMedicineAisle(aisle: Aisle) { medicine = medicine.copy(aisle = aisle) }
    fun updateMedicineStock(stock: Int) { medicine = medicine.copy(stock = stock) }

    /**
     * Attempts to add the current medicine to the repository after setting the author.
     */
    fun addMedicine(
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
                                author = user,
                                type = MedicineChangeType.CREATION,
                                newStock = medicine.stock
                            )
                        )
                    )).fold(
                        onSuccess = {
                            addOrEditMedicineUiState = UiState.Success(Unit)
                            withContext(layoutDispatcher) { onResult() }
                                    },
                        onFailure = {
                            showUnknownErrorToast()
                            addOrEditMedicineUiState = UiState.Idle
                        }
                    )
                }
            },
            onNoUserLogged = {
                showAuthErrorToast()
            }
        )
    }

    fun updateMedicine(
        dataDispatcher: CoroutineDispatcher = Dispatchers.IO,
        layoutDispatcher: CoroutineDispatcher = Dispatchers.Main,
        onResult: () -> Unit
    ) {
        addOrEditMedicineUiState = UiState.Loading
        log.d("MedicineViewModel::updateMedicine: addOrEditMedicineUiState = $addOrEditMedicineUiState")
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

                    val changeRecordType : MedicineChangeType = when {
                        medicine.stock < sourceMedicine.stock -> MedicineChangeType.STOCK_DECREASE
                        medicine.stock > sourceMedicine.stock -> MedicineChangeType.STOCK_INCREASE
                        medicine.stock == sourceMedicine.stock -> throw InvalidStockException("Stock has not been modified")
                        else -> throw InvalidStockException("Stock value is invalid")
                    }
                    log.d("MedicineViewModel::updateMedicine: changeRecordType = $changeRecordType")
                    medicineRepository.updateMedicine(medicine.copy(
                        author = user,
                        changeRecord = medicine.changeRecord +
                                MedicineChange(
                                    author = user,
                                    type  =  changeRecordType,
                                    previousStock = sourceMedicine.stock,
                                    newStock = medicine.stock
                                )
                    )).fold(
                        onSuccess = {
                            log.d("MedicineViewModel::updateMedicine: Successful")
                            addOrEditMedicineUiState = UiState.Success(Unit)
                            withContext(layoutDispatcher) { onResult() }
                                    },
                        onFailure = {
                            log.d("MedicineViewModel::updateMedicine: failed")
                            addOrEditMedicineUiState = UiState.Idle
                            showUnknownErrorToast()
                        }
                    )
                    log.d("MedicineViewModel::updateMedicine: addOrEditMedicineUiState = $addOrEditMedicineUiState")
                }
            },
            onNoUserLogged = {
                showAuthErrorToast()
            }
        )
    }

    fun deleteMedicine(
        dataDispatcher: CoroutineDispatcher = Dispatchers.IO,
        layoutDispatcher: CoroutineDispatcher = Dispatchers.Main,
        onResult: () -> Unit = {}
    ) {
        switchToMedicineDeletionMode()
        addOrEditMedicineUiState = UiState.Loading
        log.d("MedicineViewModel::deleteMedicine: addOrEditMedicineUiState = $addOrEditMedicineUiState")

        if (!isOnline) {
            showNetworkErrorToast()
            addOrEditMedicineUiState = UiState.Idle
            return
        }

        checkUserState(
            onUserLogged = { user ->
                viewModelScope.launch(dataDispatcher) {

                    medicineRepository.deleteMedicine(medicine.id).fold(
                        onSuccess = {
                            log.d("MedicineViewModel::deleteMedicine: Successful")
                            addOrEditMedicineUiState = UiState.Success(Unit)
                            withContext(layoutDispatcher) { onResult() }
                        },
                        onFailure = {
                            log.d("MedicineViewModel::deleteMedicine: failed")
                            addOrEditMedicineUiState = UiState.Idle
                            showUnknownErrorToast()
                        }
                    )
                    log.d("MedicineViewModel::deleteMedicine: addOrEditMedicineUiState = $addOrEditMedicineUiState")
                }
            },
            onNoUserLogged = {
                showAuthErrorToast()
            }
        )
    }
}