package com.oliviermarteaux.a055_rebonnte.ui.screen.medicine.medicineList

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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.oliviermarteaux.a055_rebonnte.R
import com.oliviermarteaux.a055_rebonnte.domain.model.Medicine
import com.oliviermarteaux.a055_rebonnte.ui.composable.RebonnteItemListBody
import com.oliviermarteaux.a055_rebonnte.ui.screen.medicine.MedicineViewModel
import com.oliviermarteaux.localshared.composables.RebonnteBottomAppBar
import com.oliviermarteaux.shared.composables.IconSource
import com.oliviermarteaux.shared.composables.SharedScaffold
import com.oliviermarteaux.shared.navigation.Screen
import com.oliviermarteaux.shared.ui.theme.SharedPadding
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicineListScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    medicineListViewModel: MedicineListViewModel= hiltViewModel(),
    medicineViewModel: MedicineViewModel,
    navigateToAddOrEditMedicineScreen: () -> Unit = {}
) {
    with(medicineListViewModel) {
        with(medicineViewModel) {

            var searchBarDisplayed by rememberSaveable { mutableStateOf(false) }
            fun toggleSearchBar() {
                searchBarDisplayed = !searchBarDisplayed
            }

            fun hideSearchBar() {
                searchBarDisplayed = false
            }

            var fabDisplayed by rememberSaveable { mutableStateOf(false) }
            fun showFab() {
                fabDisplayed = true
            }

            fun hideFab() {
                fabDisplayed = false
            }

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

            val cdHomeScreen =
                stringResource(R.string.you_are_on_the_home_screen_here_you_can_browse_all_the_incoming_events)
            val cdFabButton = stringResource(R.string.add_button_double_tap_to_add_a_new_event)
            val cdCustomAccessibilityActionClear = stringResource(R.string.clear_all_text)

            SharedScaffold(
                title = stringResource(Screen.Home.titleRes),
                screenContentDescription = cdHomeScreen,
                // top app bar
                topAppBarModifier = Modifier.padding(horizontal = SharedPadding.small),
                // search bar
                query = queryFieldValue,
                onQueryChange = ::filterMedicines,
                searchLabel = stringResource(R.string.look_for_an_event),
                searchBarIcon = IconSource.VectorIcon(Icons.Default.Clear),
                searchBarIconSemantics = cdCustomAccessibilityActionClear,
                onSearchBarIconClick = { clearQuery(); hideSearchBar() },
                toggleSearchBar = ::toggleSearchBar,
                searchBarDisplayed = searchBarDisplayed,
                onSearch = { focusOnSearchResult() },
                // sort menu
                onSortByNoneClick = { sortMedicinesBy(MedicineSortOption.NONE) },
                onSortByNameClick = { sortMedicinesBy(MedicineSortOption.NAME) },
                onSortByAscendingStockClick = { sortMedicinesBy(MedicineSortOption.ASCENDING_STOCK) },
                onSortByDescendingStockClick = { sortMedicinesBy(MedicineSortOption.DESCENDING_STOCK) },
                // bottom app bar
                bottomBar = { RebonnteBottomAppBar(navController) },
                // fab button
                fabVisible = fabDisplayed,
                fabContentDescription = cdFabButton,
                fabModifier = modifier.testTag("Add"),
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
                    itemList =  filteredMedicineList,
                    itemTitle =  { medicine: Medicine -> medicine.name },
                    itemText = { medicine: Medicine ->
                        stringResource(R.string.stock, medicine.stock)
                    },
                    onSearchFocusRequester = onSearchFocusRequester,
                    reloadItemOnError = ::loadMedicines,
                    showFab = ::showFab,
                    hideFab = ::hideFab
                ){ medicine ->
                    hideSearchBar()
                    selectMedicine(medicine)
                    switchToMedicineEditionMode()
                    navigateToAddOrEditMedicineScreen()
                }
//                Box(
//                    modifier = Modifier.testTag("MedicineListScreen")
//                ) {
//                    //_ UiState management: Empty, Error, Loading, Success
//                    val cdLoadingState =
//                        stringResource(R.string.please_wait_server_connection_in_progress)
//                    when (medicineListUiState) {
//                        is ListUiState.Loading -> {
//                            hideFab()
//                            CenteredCircularProgressIndicator(
//                                modifier = Modifier.semantics(
//                                    properties = {
//                                        contentDescription = cdLoadingState
//                                    }
//                                )
//                            )
//                        }
//
//                        is ListUiState.Empty -> {
//                            showFab()
//                            SharedToast("No medicine available")
//                        }
//
//                        is ListUiState.Error -> {
//                            hideFab()
//                            ErrorScreen(
//                                modifier = modifier,
//                                contentPadding = contentPadding,
//                                loadMedicines = ::loadMedicines
//                            )
//                        }
//
//                        is ListUiState.Success -> {
//                            showFab()
//                            MedicineList(
//                                modifier = modifier
//                                    .focusRequester(onSearchFocusRequester)
//                                    .focusable()
//                                    .consumeWindowInsets(contentPadding)   // 👈 prevents double padding,
//                                    .fillMaxWidth()
//                                    .padding(contentPadding)
//                                    .padding(horizontal = SharedPadding.large),
//                                medicineList = filteredMedicineList,
//                                navigateToAddOrEditMedicineScreen = navigateToAddOrEditMedicineScreen,
//                                switchToMedicineEditionMode = ::switchToMedicineEditionMode,
//                                selectMedicine = ::selectMedicine,
//                                hideSearchBar = ::hideSearchBar
//                            )
//                        }
//                    }
//                    if (authError) SharedToast(
//                        text = stringResource(R.string.an_account_is_mandatory_to_add_or_edit_a_medicine),
//                        bottomPadding = ToastPadding.high
//                    )
//                    if (networkError) SharedToast(
//                        text = stringResource(R.string.network_error_check_your_internet_connection),
//                        bottomPadding = ToastPadding.veryHigh
//                    )
//                }
            }
        }
    }
}
//
//@Composable
//private fun MedicineList(
//    modifier: Modifier = Modifier,
//    medicineList: List<Medicine>,
//    switchToMedicineEditionMode: () -> Unit,
//    navigateToAddOrEditMedicineScreen: () -> Unit,
//    selectMedicine: (Medicine) -> Unit,
//    hideSearchBar: () -> Unit
//) {
//    Column (modifier = modifier ) {
//        LazyColumn(
//            verticalArrangement = Arrangement.spacedBy(SharedPadding.xs),
//            modifier = Modifier.semantics{
//                collectionInfo = CollectionInfo(
//                    rowCount = medicineList.size,
//                    columnCount = 1
//                )
//            }
//        ) {
//            itemsIndexed(medicineList) { index, medicine ->
//                MedicineCell(
//                    medicine = medicine,
//                    onClick = {
//                        hideSearchBar()
//                        selectMedicine(medicine)
//                        switchToMedicineEditionMode()
//                        navigateToAddOrEditMedicineScreen()
//                    },
//                    modifier = Modifier.semantics {
//                        collectionItemInfo = CollectionItemInfo(index, 1, 0, 1)
//                    }
//                )
//            }
//        }
//    }
//}
//
///**
// * A composable that displays a single medicine in the home feed.
// *
// * @param medicine The medicine to display.
// */
//@Composable
//private fun MedicineCell(
//    medicine: Medicine,
//    onClick: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    ElevatedCard(
//        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp),
//        modifier = modifier
//            .fillMaxWidth()
//            .height(80.dp),
//        onClick = onClick
//    ) {
//        Row (
//            verticalAlignment = Alignment.CenterVertically,
//        ){
//            SpacerMedium()
//            Column(
//                modifier = Modifier
//                    .weight(1f)
//                    .padding(SharedPadding.medium),
//            ) {
//                TextTitleMedium(text = medicine.name)
//                Spacer(Modifier.padding(SharedPadding.xxs))
//
//                TextTitleSmall(text = stringResource(R.string.stock, medicine.stock))
//            }
//        }
//    }
//}
//
//@Composable
//fun ErrorScreen(
//    modifier: Modifier = Modifier,
//    contentPadding: PaddingValues,
//    loadMedicines: () -> Unit
//){
//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center,
//        modifier = modifier
//            .consumeWindowInsets(contentPadding)   // 👈 prevents double padding,
//            .fillMaxSize()
//            .padding(contentPadding)
//            .padding(horizontal = 126.dp),
//    ){
//        Box(
//            contentAlignment = Alignment.Center,
//            modifier = Modifier
//                .size(64.dp)
//                .background(color = Grey40, shape = CircleShape)
//        ) {
//            SharedIcon(
//                icon = IconSource.VectorIcon(Icons.Filled.PriorityHigh),
//                modifier = Modifier.size(32.dp),
//                tint = White,
//            )
//        }
//        SpacerLarge()
//        TextTitleMedium(text = stringResource(R.string.error))
//        TextTitleSmall(
//            text = stringResource(R.string.an_error_as_occurred_please_try_again_later),
//            textAlign = TextAlign.Center
//        )
//        Spacer(modifier = Modifier.height(35.dp))
//        SharedButton(
//            text = stringResource(R.string.try_again),
//            onClick = loadMedicines,
//            shape = MaterialTheme.shapes.extraSmall,
//            colors = ButtonDefaults.buttonColors(containerColor = Red40),
//            textColor = White
//        )
//    }
//}