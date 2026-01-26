package com.oliviermarteaux.a055_rebonnte.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.oliviermarteaux.a055_rebonnte.domain.model.Medicine
import com.oliviermarteaux.a055_rebonnte.ui.CrudAction
import com.oliviermarteaux.a055_rebonnte.ui.MedicineSortOption
import com.oliviermarteaux.a055_rebonnte.ui.composable.RebonnteBottomAppBar
import com.oliviermarteaux.a055_rebonnte.ui.composable.RebonnteItemListBody
import com.oliviermarteaux.a055_rebonnte.ui.navigation.RebonnteBottomNavItem
import com.oliviermarteaux.a055_rebonnte.ui.navigation.RebonnteScreen
import com.oliviermarteaux.a055_rebonnte.ui.viewModel.AisleListViewModel
import com.oliviermarteaux.a055_rebonnte.ui.viewModel.MedicineListViewModel
import com.oliviermarteaux.a055_rebonnte.ui.viewModel.MedicineViewModel
import com.oliviermarteaux.shared.composables.SharedScaffold
import com.oliviermarteaux.shared.composables.IconSource
import com.oliviermarteaux.shared.ui.UiState
import com.oliviermarteaux.shared.ui.theme.SharedPadding
import kotlinx.coroutines.delay
import com.oliviermarteaux.shared.compose.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicineListScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    //homeViewModel: AisleListViewModel = hiltViewModel(), // for medicine pre-populating only
    medicineListViewModel: MedicineListViewModel,
    medicineViewModel: MedicineViewModel,
    aisleListViewModel: AisleListViewModel,
    navigateToAddOrEditMedicineScreen: () -> Unit = {}
) {
    with(medicineListViewModel) {
        with(medicineViewModel) {

            var searchBarDisplayed by rememberSaveable { mutableStateOf(false) }
            fun toggleSearchBar() { searchBarDisplayed = !searchBarDisplayed }
            fun hideSearchBar() { searchBarDisplayed = false }

            var fabDisplayed by rememberSaveable { mutableStateOf(false) }
            fun showFab() { fabDisplayed = true }
            fun hideFab() { fabDisplayed = false }

            val onSearchFocusRequester = remember { FocusRequester() }
            var searchResultFocused by mutableStateOf(false)
            fun focusOnSearchResult() {
                Log.d("OM_TAG", "HomeScreen: focusOnSearchResult")
                searchResultFocused = !searchResultFocused
            }

            LaunchedEffect(searchResultFocused) {
                delay(1000)
                Log.d(
                    "OM_TAG",
                    "HomeScreen: LaunchedEffect: searchResultFocused = $searchResultFocused"
                )
                onSearchFocusRequester.requestFocus()
            }

            //_ talkback content descriptions
            val cdScreenTitle = stringResource(RebonnteScreen.MedicineList.titleRes)
            val cdItem = stringResource(R.string.medicine)
            val cdItems = stringResource(R.string.medicines)
            val cdScreen =
                if (addOrEditMedicineUiState is UiState.Success) {
//                    resetAddOrEditMedicineUiState()
                    when (medicineCrudAction){
                        CrudAction.ADD -> stringResource(R.string.successfully_created, cdItem, medicine.name)
                        CrudAction.UPDATE -> stringResource(R.string.successfully_edited, cdItem, medicine.name)
                        CrudAction.DELETE -> stringResource(R.string.successfully_deleted, cdItem, medicine.name)
                        else -> ""
                    }
                } else
                    stringResource(
                        R.string.you_are_on_the_screen_here_you_can_browse_all_the,
                        cdScreenTitle,
                        cdItems
                    )
            val cdFabLabel = stringResource(R.string.add_a, cdItem)
            val cdFabAction = stringResource(R.string.add_a_new, cdItem)
            val cdFabButton =
                stringResource(R.string.button_double_tap_to, cdFabLabel, cdFabAction)
            val cdCustomAccessibilityActionClear = stringResource(R.string.clear_all_text)

            SharedScaffold(
                title = stringResource(RebonnteScreen.MedicineList.titleRes),
                screenContentDescription = cdScreen,
                // top app bar
                topAppBarModifier = Modifier.padding(horizontal = SharedPadding.small),
                // search bar
                query = queryFieldValue,
                onQueryChange = ::filterMedicineByName,
                searchLabel = stringResource(R.string.look_for_a, cdItem),
                searchBarIcon = IconSource.VectorIcon(Icons.Default.Clear),
                searchBarIconSemantics = cdCustomAccessibilityActionClear,
                onSearchBarIconClick = { clearQuery(); hideSearchBar() },
                searchBarIconModifier = Modifier.testTag("SearchBarClearIcon"),
                searchBarTextFieldModifier = Modifier.testTag("SearchField"),
                toggleSearchBar = ::toggleSearchBar,
                searchBarDisplayed = searchBarDisplayed,
                onSearch = { focusOnSearchResult() },
                // sort menu
                onSortByNoneClick = { sortMedicinesBy(MedicineSortOption.DESCENDING_TIMESTAMP) },
                onSortByNameClick = { sortMedicinesBy(MedicineSortOption.ASCENDING_NAME) },
                onSortByAscendingStockClick = { sortMedicinesBy(MedicineSortOption.ASCENDING_STOCK) },
                onSortByDescendingStockClick = { sortMedicinesBy(MedicineSortOption.DESCENDING_STOCK) },
                // bottom app bar
                bottomBar = { RebonnteBottomAppBar(
                    navController = navController,
                    item1 = RebonnteBottomNavItem.AisleNavItem,
                    callback1 = {
                        aisleListViewModel.loadFirstPage()
                    },
                    item2 = RebonnteBottomNavItem.MedicineNavItem
                )},
                // fab button
                fabVisible = fabDisplayed,
                fabContentDescription = cdFabButton,
                fabModifier = modifier.testTag("AddMedicine"),
                onFabClick = //{populateFakeMedicineListForDemo(homeViewModel.aisleList)}
                    {
                        checkUserState(
                            onUserLogged = {
                                hideSearchBar()
                                aisleListViewModel.getAllAisle()
                                selectMedicine(Medicine())
                                switchToMedicineCreationMode()
                                navigateToAddOrEditMedicineScreen()
                            },
                            onNoUserLogged = ::showAuthErrorToast
                        )
                    }
            ) { contentPadding ->
                LaunchedEffect(medicineListUiState) {
                    Log.i(
                        "OM_TAG",
                        "MedicineListViewModel: LaunchedEffect: medicineListUiState = $medicineListUiState"
                    )
                }
                RebonnteItemListBody(
                    contentPadding = contentPadding,
                    modifier = modifier,
                    testTag = "MedicineListScreen",
                    listUiState = medicineListUiState,
                    listViewModel = medicineListViewModel,
                    itemLabel = stringResource(R.string.medicine),
                    itemList =  medicineList,
                    item = medicine,
                    itemId =  Medicine::id,
                    itemTitle =  Medicine::name,
                    itemText = { medicine: Medicine ->
                        stringResource(R.string.stock_value, medicine.stock) },
                    onSearchFocusRequester = onSearchFocusRequester,
                    reloadItemList = ::getAllMedicineByDescendingTimestamp,
                    showFab = ::showFab,
                    hideFab = ::hideFab,
                    actionUiState = addOrEditMedicineUiState,
                    itemCrudAction = medicineCrudAction,
                    resetUiState = ::resetAddOrEditMedicineUiState,
                    resetItemCrudAction = ::resetMedicineCrudAction,
                    isLastPage = isLastPage,
                    loadNextPage = ::loadNextPage,
                    itemModifier = Modifier.testTag("MedicineItem"),
                    lazyListModifier = Modifier.testTag("MedicineLazyList")
                ){ medicine ->
                    hideSearchBar()
                    selectMedicine(medicine)
                    switchToMedicineEditionMode()
                    navigateToAddOrEditMedicineScreen()
                }
            }
        }
    }
}