package com.oliviermarteaux.a055_rebonnte.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.oliviermarteaux.a055_rebonnte.domain.model.Aisle
import com.oliviermarteaux.a055_rebonnte.ui.CrudAction
import com.oliviermarteaux.a055_rebonnte.ui.composable.RebonnteItemListBody
import com.oliviermarteaux.a055_rebonnte.ui.navigation.RebonnteScreen
import com.oliviermarteaux.a055_rebonnte.ui.composable.RebonnteBottomAppBar
import com.oliviermarteaux.a055_rebonnte.ui.navigation.RebonnteBottomNavItem
import com.oliviermarteaux.a055_rebonnte.ui.viewModel.AisleListViewModel
import com.oliviermarteaux.a055_rebonnte.ui.viewModel.AisleViewModel
import com.oliviermarteaux.a055_rebonnte.ui.viewModel.MedicineListViewModel
import com.oliviermarteaux.shared.composables.SharedScaffold
import com.oliviermarteaux.shared.ui.UiState
import com.oliviermarteaux.shared.ui.theme.SharedPadding
import com.oliviermarteaux.shared.compose.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AisleListScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    aisleListViewModel: AisleListViewModel,
    aisleViewModel: AisleViewModel,
    medicineListViewModel: MedicineListViewModel,
    navigateToDetailScreen: () -> Unit,
    navigateToAddScreen: () -> Unit
) {
    with(aisleListViewModel) {
        with (aisleViewModel) {

            var fabDisplayed by rememberSaveable { mutableStateOf(false) }
            fun showFab(){ fabDisplayed = true }
            fun hideFab(){ fabDisplayed = false }

            val cdItem = stringResource(R.string.aisle)
            val cdItems = stringResource(R.string.aisles)
            val cdScreenTitle = stringResource(RebonnteScreen.AisleList.titleRes)
            val cdScreen =
                if (addAisleUiState is UiState.Success) {
                    resetAddAisleUiState()
                    stringResource(R.string.successfully_created, aisle.name, cdItem)
                }
                else
                    stringResource(
                        R.string.you_are_on_the_screen_here_you_can_browse_all_the,
                        cdScreenTitle,
                        cdItems
                    )
            val cdFabLabel = stringResource(R.string.add_an, cdItem)
            val cdFabAction = stringResource(R.string.add_a_new, cdItem)
            val cdFabButton =
                stringResource(R.string.button_double_tap_to, cdFabLabel, cdFabAction)

            SharedScaffold(
                title = stringResource(RebonnteScreen.AisleList.titleRes),
                screenContentDescription = cdScreen,
                // top app bar
                topAppBarModifier = Modifier.padding(horizontal = SharedPadding.small),
                // bottom app bar
                bottomBar = { RebonnteBottomAppBar(
                    navController = navController,
                    item1 = RebonnteBottomNavItem.AisleNavItem,
                    item2 = RebonnteBottomNavItem.MedicineNavItem,
                    callback2 = medicineListViewModel::getAllMedicineByDescendingTimestamp
                )},
                // fab button
                fabVisible = fabDisplayed,
                fabContentDescription = cdFabButton,
                fabModifier = modifier.testTag("AddAisle"),
                onFabClick = //::populateFakeAisleListForDemo
                    {
                        checkUserState(
                            onUserLogged = {
                                selectAisle(Aisle())
                                navigateToAddScreen()
                                           },
                            onNoUserLogged = ::showAuthErrorToast
                        )
                    }
            ) { contentPadding ->
                LaunchedEffect(homeUiState) {
                    Log.i(
                        "OM_TAG",
                        "HomeFeedViewModel: LaunchedEffect: homeFeedUiState = $homeUiState"
                    )
                }
                RebonnteItemListBody(
                    contentPadding = contentPadding,
                    modifier = modifier,
                    testTag = "MedicineListScreen",
                    listUiState = homeUiState,
                    listViewModel = aisleListViewModel,
                    itemLabel = stringResource(R.string.aisle),
                    itemList =  aisleList,
                    item = aisle,
                    itemId = Aisle::id,
                    itemTitle =  Aisle::name,
                    reloadItemList = ::loadFirstPage,
                    showFab = ::showFab,
                    hideFab = ::hideFab,
                    actionUiState = addAisleUiState,
                    resetUiState = ::resetAddAisleUiState,
                    itemCrudAction = CrudAction.ADD,
                    isLastPage = isLastPage,
                    loadNextPage = ::loadNextPage
                ){ aisle ->
                    selectAisle(aisle)
                    medicineListViewModel.filterMedicineByAisleId(aisle.id)
                    navigateToDetailScreen()
                }
            }
        }
    }
}