package com.oliviermarteaux.a055_rebonnte.ui.screen.medicineList

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
import com.oliviermarteaux.a055_rebonnte.R
import com.oliviermarteaux.a055_rebonnte.domain.model.Medicine
import com.oliviermarteaux.a055_rebonnte.ui.composable.RebonnteItemListBody
import com.oliviermarteaux.a055_rebonnte.ui.navigation.RebonnteScreen
import com.oliviermarteaux.a055_rebonnte.ui.screen.MedicineListViewModel
import com.oliviermarteaux.a055_rebonnte.ui.screen.MedicineSortOption
import com.oliviermarteaux.a055_rebonnte.ui.screen.MedicineViewModel
import com.oliviermarteaux.localshared.composables.RebonnteBottomAppBar
import com.oliviermarteaux.shared.composables.IconSource
import com.oliviermarteaux.shared.composables.SharedScaffold
import com.oliviermarteaux.shared.ui.theme.SharedPadding
import kotlinx.coroutines.delay
import com.oliviermarteaux.shared.compose.R as oR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicineListScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    medicineListViewModel: MedicineListViewModel,
    medicineViewModel: MedicineViewModel,
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
            val cdItems = stringResource(R.string.medicines)
            val cdScreen = stringResource(
                R.string.you_are_on_the_screen_here_you_can_browse_all_the,
                cdScreenTitle,
                cdItems
            )
            val cdItem = stringResource(R.string.medicine)
            val cdFabLabel = stringResource(R.string.add_a, cdItem)
            val cdFabAction = stringResource(R.string.add_a_new, cdItem)
            val cdFabButton =
                stringResource(R.string.button_double_tap_to, cdFabLabel, cdFabAction)
            val cdCustomAccessibilityActionClear = stringResource(oR.string.clear_all_text)

            SharedScaffold(
                title = stringResource(RebonnteScreen.MedicineList.titleRes),
                screenContentDescription = cdScreen,
                // top app bar
                topAppBarModifier = Modifier.padding(horizontal = SharedPadding.small),
                // search bar
                query = queryFieldValue,
                onQueryChange = ::filterMedicines,
                searchLabel = stringResource(R.string.look_for_an, cdItem),
                searchBarIcon = IconSource.VectorIcon(Icons.Default.Clear),
                searchBarIconSemantics = cdCustomAccessibilityActionClear,
                onSearchBarIconClick = { clearQuery(); hideSearchBar() },
                toggleSearchBar = ::toggleSearchBar,
                searchBarDisplayed = searchBarDisplayed,
                onSearch = { focusOnSearchResult() },
                // sort menu
                onSortByNoneClick = ::loadMedicines,
                onSortByNameClick = { sortMedicinesBy(MedicineSortOption.NAME) },
                onSortByAscendingStockClick = { sortMedicinesBy(MedicineSortOption.ASCENDING_STOCK) },
                onSortByDescendingStockClick = { sortMedicinesBy(MedicineSortOption.DESCENDING_STOCK) },
                // bottom app bar
                bottomBar = { RebonnteBottomAppBar(navController) },
                // fab button
                fabVisible = fabDisplayed,
                fabContentDescription = cdFabButton,
                fabModifier = modifier.testTag("MedicineListScreenFab"),
                onFabClick = {
                    checkUserState(
                        onUserLogged = {
                            hideSearchBar()
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
                    itemList =  filteredMedicineList,
                    itemTitle =  Medicine::name,
                    itemText = { medicine: Medicine ->
                        stringResource(R.string.stock, medicine.stock) },
                    onSearchFocusRequester = onSearchFocusRequester,
                    reloadItemOnError = ::loadMedicines,
                    showFab = ::showFab,
                    hideFab = ::hideFab,
                    actionUiState = addOrEditMedicineUiState,
                    actionCreation = medicineCreation
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