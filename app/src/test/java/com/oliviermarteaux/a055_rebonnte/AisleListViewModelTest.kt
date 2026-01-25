package com.oliviermarteaux.a055_rebonnte

import com.oliviermarteaux.a055_rebonnte.data.repository.AisleRepository
import com.oliviermarteaux.a055_rebonnte.domain.model.Aisle
import com.oliviermarteaux.a055_rebonnte.ui.PagedList
import com.oliviermarteaux.a055_rebonnte.ui.viewModel.AisleListViewModel
import com.oliviermarteaux.shared.firebase.authentication.data.repository.UserRepository
import com.oliviermarteaux.shared.test.rule.MainDispatcherRule
import com.oliviermarteaux.shared.ui.ListUiState
import com.oliviermarteaux.shared.utils.Logger
import com.oliviermarteaux.shared.utils.NoOpLogger
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
class AisleListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private val aisleRepository: AisleRepository = mockk()
    private val userRepository: UserRepository = mockk(relaxed = true)
    private val logger: Logger = NoOpLogger
    private val isOnlineFlow = MutableStateFlow(true)
    private lateinit var viewModel: AisleListViewModel

    private fun createViewModel() {
        viewModel = AisleListViewModel(
            aisleRepository = aisleRepository,
            userRepository = userRepository,
            log = logger,
            isOnlineFlow = isOnlineFlow
        )
    }

    @Test
    fun aisleListViewModel_loadFirstPage_emptyResult_emitsEmptyState() = runTest {
        // Given
        val emptyPage = PagedList<Aisle>(
            items = emptyList(),
            lastSnapshot = null,
            isLastPage = true
        )

        every {
            aisleRepository.getAislePaged(
                pageSize = any(),
                lastSnapshot = null
            )
        } returns flowOf(Result.success(emptyPage))

        createViewModel()

        // When
        viewModel.loadFirstPage()
        advanceUntilIdle()

        // Then
        assertTrue(viewModel.aisleList.isEmpty())
        assertTrue(viewModel.isLastPage)
        assertFalse(viewModel.isLoading)
        assertTrue(viewModel.homeUiState is ListUiState.Empty)
    }

    @Test
    fun aisleListViewModel_loadFirstPage_repositoryFailure_emitsErrorState() = runTest {
        // Given
        val exception = IllegalStateException("Firestore failure")

        every {
            aisleRepository.getAislePaged(
                pageSize = any(),
                lastSnapshot = null
            )
        } returns flowOf(Result.failure(exception))

        createViewModel()

        // When
        viewModel.loadFirstPage()
        advanceUntilIdle()

        // Then
        assertTrue(viewModel.aisleList.isEmpty())
        assertFalse(viewModel.isLastPage)
        assertFalse(viewModel.isLoading)

        val uiState = viewModel.homeUiState
        assertTrue(uiState is ListUiState.Error)
        assertEquals(exception, (uiState as ListUiState.Error).throwable)
    }
}