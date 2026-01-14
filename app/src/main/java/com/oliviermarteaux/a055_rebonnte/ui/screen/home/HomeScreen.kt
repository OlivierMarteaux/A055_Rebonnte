package com.oliviermarteaux.a055_rebonnte.ui.screen.home

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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.oliviermarteaux.a055_rebonnte.R
import com.oliviermarteaux.a055_rebonnte.domain.model.Aisle
import com.oliviermarteaux.a055_rebonnte.ui.composable.RebonnteItemListBody
import com.oliviermarteaux.a055_rebonnte.ui.screen.AisleViewModel
import com.oliviermarteaux.localshared.composables.RebonnteBottomAppBar
import com.oliviermarteaux.shared.composables.SharedScaffold
import com.oliviermarteaux.shared.navigation.Screen
import com.oliviermarteaux.shared.ui.theme.SharedPadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel(),
    aisleViewModel: AisleViewModel,
    navigateToDetailScreen: () -> Unit = {},
    navigateToAddScreen: () -> Unit = {}
) {
    with(homeViewModel) {
        with (aisleViewModel) {

            var fabDisplayed by rememberSaveable { mutableStateOf(false) }
            fun showFab(){ fabDisplayed = true }
            fun hideFab(){ fabDisplayed = false }

            val cdHomeScreen =
                stringResource(R.string.you_are_on_the_home_screen_here_you_can_browse_all_the_incoming_events)
            val cdFabButton = stringResource(R.string.add_button_double_tap_to_add_a_new_event)

            SharedScaffold(
                title = stringResource(Screen.Home.titleRes),
                screenContentDescription = cdHomeScreen,
                // top app bar
                topAppBarModifier = Modifier.padding(horizontal = SharedPadding.small),
                // bottom app bar
                bottomBar = { RebonnteBottomAppBar(navController) },
                // fab button
                fabVisible = fabDisplayed,
                fabContentDescription = cdFabButton,
                fabModifier = modifier.testTag("Add"),
                onFabClick = {
                    checkUserState(
                        onUserLogged = { navigateToAddScreen() },
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
                    listViewModel = homeViewModel,
                    itemList =  aisleList,
                    itemTitle =  { aisle: Aisle -> aisle.name },
                    reloadItemOnError = ::loadAisles,
                    showFab = ::showFab,
                    hideFab = ::hideFab
                ){ aisle ->
                    selectAisle(aisle)
                    navigateToDetailScreen()
                }
            }
        }
    }
}