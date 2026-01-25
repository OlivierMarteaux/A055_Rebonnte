package com.oliviermarteaux.a055_rebonnte

import com.oliviermarteaux.a055_rebonnte.data.repository.MedicineRepository
import com.oliviermarteaux.a055_rebonnte.domain.model.Medicine
import com.oliviermarteaux.a055_rebonnte.ui.viewModel.MedicineViewModel
import com.oliviermarteaux.shared.firebase.authentication.data.repository.UserRepository
import com.oliviermarteaux.shared.firebase.authentication.domain.model.User
import com.oliviermarteaux.shared.test.rule.MainDispatcherRule
import com.oliviermarteaux.shared.ui.UiState
import com.oliviermarteaux.shared.utils.Logger
import com.oliviermarteaux.shared.utils.NoOpLogger
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MedicineViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private val medicineRepository: MedicineRepository = mockk()
    private val userRepository: UserRepository = mockk()
    private val logger: Logger = NoOpLogger
    private lateinit var isOnlineFlow: MutableStateFlow<Boolean>
    private lateinit var viewModel: MedicineViewModel
    private val connectedUser = User(email = "test@test.com")
    private val connectedFlow = MutableStateFlow<User?>(connectedUser)
    private val notConnectedFlow = MutableStateFlow<User?>(null)

    private fun createViewModel() {
        viewModel = MedicineViewModel(
            medicineRepository = medicineRepository,
            userRepository = userRepository,
            isOnlineFlow = isOnlineFlow,
            log = logger
        )
    }

    //_ #############################
    //_ ADD
    //_ #############################
    @Test
    fun medicineViewModel_addMedicine_offline_doesNotCallRepository_ResetsUiState_TriggerToast() = runTest {
        // Given
        isOnlineFlow = MutableStateFlow(false)
        val testDispatcher = mainDispatcherRule.testDispatcher
        every { userRepository.userAuthState } returns connectedFlow

        createViewModel()

        //_ Make sure state is collected
        advanceUntilIdle() //_ <- critical

        var onResultCalled = false

        // When
        viewModel.addMedicine(
            dataDispatcher = testDispatcher,
            layoutDispatcher = testDispatcher,
            onResult = {
                onResultCalled = true
            }
        )
        // Then
        // Check the network error toast is triggered
        advanceTimeBy(2000)
        assertTrue(viewModel.networkError)
        advanceTimeBy(2000)
        assertFalse(viewModel.networkError)

        advanceUntilIdle()

        // check other criteria after Idle
        coVerify(exactly = 0) { medicineRepository.addMedicine(any()) }
        assertTrue(viewModel.addOrEditMedicineUiState is UiState.Idle)
        assertFalse(onResultCalled)
    }

    @Test
    fun medicineViewModel_addMedicine_noUserLogged_doesNotCallRepository_ResetsUiState_TriggerToast() = runTest {
        // Given
        isOnlineFlow = MutableStateFlow(true)
        val testDispatcher = mainDispatcherRule.testDispatcher
        every { userRepository.userAuthState } returns notConnectedFlow

        createViewModel()

        //_ Make sure state is collected
        advanceUntilIdle() //_ <- critical

        var onResultCalled = false

        // When
        viewModel.addMedicine(
            dataDispatcher = testDispatcher,
            layoutDispatcher = testDispatcher,
            onResult = {onResultCalled = true}
        )

        // Then
        // Check the network error toast is triggered
        advanceTimeBy(2000)
        assertTrue(viewModel.authError)
        advanceTimeBy(2000)
        assertFalse(viewModel.authError)

        advanceUntilIdle()

        coVerify(exactly = 0) { medicineRepository.addMedicine(any()) }
        assertTrue(viewModel.addOrEditMedicineUiState is UiState.Idle)
        assertFalse(onResultCalled)
    }

    @Test
    fun medicineViewModel_addMedicine_repositoryFailure_ResetsUiState_TriggerToast() = runTest {
        // Given
        isOnlineFlow = MutableStateFlow(true)
        val testDispatcher = mainDispatcherRule.testDispatcher
        every { userRepository.userAuthState } returns connectedFlow
        coEvery { medicineRepository.addMedicine(any()) } returns
                Result.failure(RuntimeException("boom"))

        createViewModel()

        //_ Make sure state is collected
        advanceUntilIdle() //_ <- critical

        var onResultCalled = false

        // When
        viewModel.addMedicine(
            dataDispatcher = testDispatcher,
            layoutDispatcher = testDispatcher,
            onResult = { onResultCalled = true }
        )

        // Then
        // Check the network error toast is triggered
        advanceTimeBy(2000)
        assertTrue(viewModel.unknownError)
        advanceTimeBy(2000)
        assertFalse(viewModel.unknownError)

        advanceUntilIdle()

        coVerify(exactly = 1) { medicineRepository.addMedicine(any()) }
        assertTrue(viewModel.addOrEditMedicineUiState is UiState.Idle)
        assertFalse(onResultCalled)
    }

    //_ #############################
    //_ UPDATE
    //_ #############################
    @Test
    fun medicineViewModel_updateMedicine_offline_doesNotCallRepository_ResetsUiState_TriggerToast() = runTest {
        // Given
        isOnlineFlow = MutableStateFlow(false)
        val testDispatcher = mainDispatcherRule.testDispatcher
        every { userRepository.userAuthState } returns connectedFlow

        createViewModel()

        //_ Make sure state is collected
        advanceUntilIdle() //_ <- critical

        var onResultCalled = false

        // When
        viewModel.updateMedicine(
            dataDispatcher = testDispatcher,
            layoutDispatcher = testDispatcher,
            onResult = {
                onResultCalled = true
            }
        )
        // Then
        // Check the network error toast is triggered
        advanceTimeBy(2000)
        assertTrue(viewModel.networkError)
        advanceTimeBy(2000)
        assertFalse(viewModel.networkError)

        advanceUntilIdle()

        // check other criteria after Idle
        coVerify(exactly = 0) { medicineRepository.updateMedicine(any()) }
        assertTrue(viewModel.addOrEditMedicineUiState is UiState.Idle)
        assertFalse(onResultCalled)
    }

    @Test
    fun medicineViewModel_updateMedicine_noUserLogged_doesNotCallRepository_ResetsUiState_TriggerToast() = runTest {
        // Given
        isOnlineFlow = MutableStateFlow(true)
        val testDispatcher = mainDispatcherRule.testDispatcher
        every { userRepository.userAuthState } returns notConnectedFlow

        createViewModel()

        //_ Make sure state is collected
        advanceUntilIdle() //_ <- critical

        var onResultCalled = false

        // When
        viewModel.updateMedicine(
            dataDispatcher = testDispatcher,
            layoutDispatcher = testDispatcher,
            onResult = {onResultCalled = true}
        )

        // Then
        // Check the network error toast is triggered
        advanceTimeBy(2000)
        assertTrue(viewModel.authError)
        advanceTimeBy(2000)
        assertFalse(viewModel.authError)

        advanceUntilIdle()

        coVerify(exactly = 0) { medicineRepository.updateMedicine(any()) }
        assertTrue(viewModel.addOrEditMedicineUiState is UiState.Idle)
        assertFalse(onResultCalled)
    }

    @Test
    fun medicineViewModel_updateMedicine_repositoryFailure_ResetsUiState_TriggerToast() = runTest {
        // Given
        isOnlineFlow = MutableStateFlow(true)
        val testDispatcher = mainDispatcherRule.testDispatcher
        every { userRepository.userAuthState } returns connectedFlow

        coEvery { medicineRepository.updateMedicine(any()) } returns
                Result.failure(RuntimeException("boom"))

        createViewModel()

        //_ Make sure state is collected
        advanceUntilIdle() //_ <- critical

        // 🔑 IMPORTANT: drive valid stock change
        viewModel.selectMedicine(Medicine(id = "1", stock = 5))
        viewModel.updateMedicineStock(10)

        var onResultCalled = false

        // When
        viewModel.updateMedicine(
            dataDispatcher = testDispatcher,
            layoutDispatcher = testDispatcher,
            onResult = { onResultCalled = true }
        )

        // Then
        // Check the network error toast is triggered
        advanceTimeBy(2000)
        assertTrue(viewModel.unknownError)
        advanceTimeBy(2000)
        assertFalse(viewModel.unknownError)

        advanceUntilIdle()

        coVerify(exactly = 1) { medicineRepository.updateMedicine(any()) }
        assertTrue(viewModel.addOrEditMedicineUiState is UiState.Idle)
        assertFalse(onResultCalled)
    }

    //_ #############################
    //_ DELETE
    //_ #############################
    @Test
    fun medicineViewModel_deleteMedicine_offline_doesNotCallRepository_ResetsUiState_TriggerToast() = runTest {
        // Given
        isOnlineFlow = MutableStateFlow(false)
        val testDispatcher = mainDispatcherRule.testDispatcher
        every { userRepository.userAuthState } returns connectedFlow

        createViewModel()

        //_ Make sure state is collected
        advanceUntilIdle() //_ <- critical

        var onResultCalled = false

        // When
        viewModel.deleteMedicine(
            dataDispatcher = testDispatcher,
            layoutDispatcher = testDispatcher,
            onResult = {
                onResultCalled = true
            }
        )
        // Then
        // Check the network error toast is triggered
        advanceTimeBy(2000)
        assertTrue(viewModel.networkError)
        advanceTimeBy(2000)
        assertFalse(viewModel.networkError)

        advanceUntilIdle()

        // check other criteria after Idle
        coVerify(exactly = 0) { medicineRepository.deleteMedicine(any()) }
        assertTrue(viewModel.addOrEditMedicineUiState is UiState.Idle)
        assertFalse(onResultCalled)
    }

    @Test
    fun medicineViewModel_deleteMedicine_noUserLogged_doesNotCallRepository_ResetsUiState_TriggerToast() = runTest {
        // Given
        isOnlineFlow = MutableStateFlow(true)
        val testDispatcher = mainDispatcherRule.testDispatcher
        every { userRepository.userAuthState } returns notConnectedFlow

        createViewModel()

        //_ Make sure state is collected
        advanceUntilIdle() //_ <- critical

        var onResultCalled = false

        // When
        viewModel.deleteMedicine(
            dataDispatcher = testDispatcher,
            layoutDispatcher = testDispatcher,
            onResult = {onResultCalled = true}
        )

        // Then
        // Check the network error toast is triggered
        advanceTimeBy(2000)
        assertTrue(viewModel.authError)
        advanceTimeBy(2000)
        assertFalse(viewModel.authError)

        advanceUntilIdle()

        coVerify(exactly = 0) { medicineRepository.deleteMedicine(any()) }
        assertTrue(viewModel.addOrEditMedicineUiState is UiState.Idle)
        assertFalse(onResultCalled)
    }

    @Test
    fun medicineViewModel_deleteMedicine_repositoryFailure_ResetsUiState_TriggerToast() = runTest {
        // Given
        isOnlineFlow = MutableStateFlow(true)
        val testDispatcher = mainDispatcherRule.testDispatcher
        every { userRepository.userAuthState } returns connectedFlow
        coEvery { medicineRepository.deleteMedicine(any()) } returns
                Result.failure(RuntimeException("boom"))

        createViewModel()

        //_ Make sure state is collected
        advanceUntilIdle() //_ <- critical

        var onResultCalled = false

        // When
        viewModel.deleteMedicine(
            dataDispatcher = testDispatcher,
            layoutDispatcher = testDispatcher,
            onResult = { onResultCalled = true }
        )

        // Then
        // Check the network error toast is triggered
        advanceTimeBy(2000)
        assertTrue(viewModel.unknownError)
        advanceTimeBy(2000)
        assertFalse(viewModel.unknownError)

        advanceUntilIdle()

        coVerify(exactly = 1) { medicineRepository.deleteMedicine(any()) }
        assertTrue(viewModel.addOrEditMedicineUiState is UiState.Idle)
        assertFalse(onResultCalled)
    }
}
