package com.oliviermarteaux.a055_rebonnte

import com.oliviermarteaux.a055_rebonnte.data.repository.AisleRepository
import com.oliviermarteaux.a055_rebonnte.domain.model.Aisle
import com.oliviermarteaux.a055_rebonnte.ui.viewModel.AisleViewModel
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
class AisleViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private val aisleRepository: AisleRepository = mockk()
    private val userRepository: UserRepository = mockk()
    private val logger: Logger = NoOpLogger
    private lateinit var isOnlineFlow: MutableStateFlow<Boolean>
    private lateinit var viewModel: AisleViewModel

    private fun createViewModel() {
        viewModel = AisleViewModel(
            aisleRepository = aisleRepository,
            userRepository = userRepository,
            isOnlineFlow = isOnlineFlow,
            log = logger
        )
    }

    @Test
    fun aisleViewModel_addAisle_noUserLogged_doesNotCallRepositoryAndResetsState() = runTest {
        // Given
        isOnlineFlow = MutableStateFlow(true)
        val testDispatcher = mainDispatcherRule.testDispatcher
        val testUser = null
        val testAisle = Aisle(name = "Test Aisle")

        every { userRepository.userAuthState } returns MutableStateFlow(testUser)
        coEvery { aisleRepository.addAisle(any()) } returns Result.success(Unit)

        createViewModel()

        //_ Make sure userAuthState is collected
        advanceUntilIdle() //_ <- critical

        viewModel.selectAisle(testAisle)

        var onResultCalled = false

        // When
        viewModel.addAisle(
            dataDispatcher = testDispatcher,
            layoutDispatcher = testDispatcher
        ) {
            onResultCalled = true
        }

        // Then
        // Check the network error toast is triggered
        advanceTimeBy(2000)
        assertTrue(viewModel.authError)
        advanceTimeBy(2000)
        assertFalse(viewModel.authError)

        advanceUntilIdle() //_ run all coroutines

        coVerify(exactly = 0) { aisleRepository.addAisle(any()) }
        assertTrue(viewModel.addAisleUiState is UiState.Idle)
        assertFalse(onResultCalled)
    }

    @Test
    fun aisleViewModel_addAisle_offline_resetsUiStateAndDoesNotCallRepository() = runTest {
        // Given
        isOnlineFlow = MutableStateFlow(false)
        val testDispatcher = mainDispatcherRule.testDispatcher
        val testUser = User(email = "test@example.com")
        val testAisle = Aisle(name = "Test Aisle")

        every { userRepository.userAuthState } returns MutableStateFlow(testUser)
        coEvery { aisleRepository.addAisle(any()) } returns Result.success(Unit)

        createViewModel()

        //_ Make sure userAuthState is collected
        advanceUntilIdle() //_ <- critical

        viewModel.selectAisle(testAisle)

        var onResultCalled = false

        // When
        viewModel.addAisle(
            dataDispatcher = testDispatcher,
            layoutDispatcher = testDispatcher
        ) {
            onResultCalled = true
        }

        // Then
        // Check the network error toast is triggered
        advanceTimeBy(2000)
        assertTrue(viewModel.networkError)
        advanceTimeBy(2000)
        assertFalse(viewModel.networkError)

        advanceUntilIdle() //_ run all coroutines

        coVerify(exactly = 0) { aisleRepository.addAisle(any()) }
        assertTrue(viewModel.addAisleUiState is UiState.Idle)
        assertFalse(onResultCalled)
    }

    @Test
    fun aisleViewModel_addAisle_repositoryFailure_ResetsUiState_TriggerToast() = runTest {
        // Given
        isOnlineFlow = MutableStateFlow(true)
        val testDispatcher = mainDispatcherRule.testDispatcher
        val testUser = User(email = "test@example.com")
        val testAisle = Aisle(name = "Test Aisle")

        every { userRepository.userAuthState } returns MutableStateFlow(testUser)
        coEvery { aisleRepository.addAisle(any()) } returns
                Result.failure(RuntimeException("boom"))

        createViewModel()

        //_ Make sure userAuthState is collected
        advanceUntilIdle() //_ <- critical

        viewModel.selectAisle(testAisle)

        var onResultCalled = false

        // When
        viewModel.addAisle(
            dataDispatcher = testDispatcher,
            layoutDispatcher = testDispatcher
        ) {
            onResultCalled = true
        }

        // Then
        // Check the network error toast is triggered
        advanceTimeBy(2000)
        assertTrue(viewModel.unknownError)
        advanceTimeBy(2000)
        assertFalse(viewModel.unknownError)

        advanceUntilIdle()

        coVerify(exactly = 1) { aisleRepository.addAisle(any()) }
        assertTrue(viewModel.addAisleUiState is UiState.Idle)
        assertFalse(onResultCalled)
    }

    @Test
    fun aisleViewModel_addAisle_userConnected_callsRepositoryAndSetsSuccessState() = runTest {
        // Given
        isOnlineFlow = MutableStateFlow(true)
        val testDispatcher = mainDispatcherRule.testDispatcher
        val testUser = User(email = "test@example.com")
        val testAisle = Aisle(name = "Test Aisle")

        every { userRepository.userAuthState } returns MutableStateFlow(testUser)
        coEvery { aisleRepository.addAisle(any()) } returns Result.success(Unit)

        createViewModel()

        //_ Make sure userAuthState is collected
        advanceUntilIdle() //_ <- critical

        viewModel.selectAisle(testAisle)

        var onResultCalled = false

        // When
        viewModel.addAisle(
            dataDispatcher = testDispatcher,
            layoutDispatcher = testDispatcher
        ) {
            onResultCalled = true
        }

        // Then
        advanceUntilIdle() //_ run all coroutines

        coVerify(exactly = 1) {
            aisleRepository.addAisle(match { it.name == "Test Aisle" && it.author == testUser })
        }
        assertTrue(viewModel.addAisleUiState is UiState.Success)
        assertTrue(onResultCalled)
    }
}