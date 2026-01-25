package com.oliviermarteaux.a055_rebonnte

import com.oliviermarteaux.a055_rebonnte.data.repository.MedicineRepository
import com.oliviermarteaux.a055_rebonnte.domain.model.Medicine
import com.oliviermarteaux.a055_rebonnte.ui.PagedList
import com.oliviermarteaux.a055_rebonnte.ui.viewModel.MedicineListViewModel
import com.oliviermarteaux.shared.firebase.authentication.data.repository.UserRepository
import com.oliviermarteaux.shared.test.rule.MainDispatcherRule
import com.oliviermarteaux.shared.ui.ListUiState
import com.oliviermarteaux.shared.utils.Logger
import com.oliviermarteaux.shared.utils.NoOpLogger
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MedicineListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private val medicineRepository: MedicineRepository = mockk()
    private val userRepository: UserRepository = mockk(relaxed = true)
    private val logger: Logger = NoOpLogger
    private lateinit var isOnlineFlow: MutableStateFlow<Boolean>
    private lateinit var viewModel: MedicineListViewModel

    private fun createViewModel() {
        viewModel = MedicineListViewModel(
            medicineRepository = medicineRepository,
            userRepository = userRepository,
            log = logger,
            isOnlineFlow = isOnlineFlow
        )
    }

    private fun stubRepositoryFlow(
        result: Result<PagedList<Medicine>>
    ) {
        every {
            medicineRepository.getMedicinesFilteredSortedPaged(
                query = any(),
                aisleId = any(),
                medicineSortOption = any(),
                pageSize = any(),
                lastSnapshot = any()
            )
        } returns flowOf(result)
    }

    @Test
    fun loadNextPage_success_updatesListAndUiState() = runTest {
        // Given
        isOnlineFlow = MutableStateFlow(true)

        val medicine1 = Medicine(id = "1", name = "Aspirin")
        val medicine2 = Medicine(id = "2", name = "Paracetamol")

        val page = PagedList(
            items = listOf(medicine1, medicine2),
            lastSnapshot = mockk(),
            isLastPage = false
        )

        stubRepositoryFlow(Result.success(page))
        createViewModel()

        //_ Make sure state is collected
        advanceUntilIdle() //_ <- critical

        // When
        viewModel.loadNextPage()

        advanceUntilIdle() //_ wait for coroutine

        // Then
        assertEquals(2, viewModel.medicineList.size)
        assertTrue(viewModel.medicineListUiState is ListUiState.Success)
        assertFalse(viewModel.isLastPage)

        verify(exactly = 1) {
            medicineRepository.getMedicinesFilteredSortedPaged(
                query = any(),
                aisleId = any(),
                medicineSortOption = any(),
                pageSize = 9,
                lastSnapshot = null
            )
        }
    }

    @Test
    fun loadNextPage_failure_setsErrorUiState() = runTest {
        // Given
        isOnlineFlow = MutableStateFlow(true)

        val error = RuntimeException("Firestore error")
        stubRepositoryFlow(Result.failure(error))
        createViewModel()

        //_ Make sure state is collected
        advanceUntilIdle() //_ <- critical

        // When
        viewModel.loadNextPage()

        advanceUntilIdle()//_ wait for coroutine

        // Then
        assertTrue(viewModel.medicineList.isEmpty())
        assertTrue(viewModel.medicineListUiState is ListUiState.Error)
        assertEquals(
            error,
            (viewModel.medicineListUiState as ListUiState.Error).throwable
        )

        verify(exactly = 1) {
            medicineRepository.getMedicinesFilteredSortedPaged(
                query = any(),
                aisleId = any(),
                medicineSortOption = any(),
                pageSize = 9,
                lastSnapshot = null
            )
        }
    }

}
