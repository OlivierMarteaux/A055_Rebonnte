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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.oliviermarteaux.a055_rebonnte.R
import com.oliviermarteaux.a055_rebonnte.domain.model.Aisle
import com.oliviermarteaux.a055_rebonnte.ui.composable.RebonnteItemListBody
import com.oliviermarteaux.a055_rebonnte.ui.screen.AisleViewModel
import com.oliviermarteaux.localshared.composables.RebonnteBottomAppBar
import com.oliviermarteaux.shared.composables.SharedScaffold
import com.oliviermarteaux.shared.navigation.Screen
import com.oliviermarteaux.shared.ui.theme.SharedPadding

/**
 * A screen that displays a feed of posts.
 *
 * @param modifier The modifier to apply to this screen.
 * @param viewModel The view model for this screen.
 * @param navigateToDetailScreen A function to call when a post is clicked.
 * @param navigateToAddScreen A function to call to navigate to the add post screen.
 */
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
//                Box(
//                    modifier = Modifier.testTag("home_screen")
//                ) {
//                    //_ UiState management: Empty, Error, Loading, Success
//                    val cdLoadingState =
//                        stringResource(R.string.please_wait_server_connection_in_progress)
//                    when (homeUiState) {
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
//                        is ListUiState.Empty -> {
//                            showFab()
//                            SharedToast(stringResource(R.string.no_posts))
//                        }
//                        is ListUiState.Error -> {
//                            hideFab()
//                            ErrorScreen(
//                                modifier = modifier,
//                                contentPadding = contentPadding,
//                                loadData = ::loadAisles
//                            )
//                        }
//
//                        is ListUiState.Success -> {
//                            showFab()
//                            HomeFeedList(
//                                modifier = modifier
//                                    .focusable()
//                                    .consumeWindowInsets(contentPadding)   // 👈 prevents double padding,
//                                    .fillMaxWidth()
//                                    .padding(contentPadding)
//                                    .padding(horizontal = SharedPadding.large),
//                                aisleList = aisleList,
//                                navigateToDetailScreen = navigateToDetailScreen,
//                                selectAisle = ::selectAisle,
//                            )
//                        }
//                    }
//                    if (authError) SharedToast(
//                        text = stringResource(R.string.an_account_is_mandatory_to_add_a_post),
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
///**
// * A composable that displays a list of aisles.
// *
// * @param modifier The modifier to apply to this composable.
// * @param aisleList The list of posts to display.
// * @param navigateToDetailScreen A function to call when a post is clicked.
// */
//@Composable
//private fun HomeFeedList(
//    modifier: Modifier = Modifier,
//    aisleList: List<Aisle>,
//    navigateToDetailScreen: () -> Unit,
//    selectAisle: (Aisle) -> Unit,
//) {
//    Column (modifier = modifier ) {
//        LazyColumn(
//            verticalArrangement = Arrangement.spacedBy(SharedPadding.xs),
//            modifier = Modifier.semantics{
//                collectionInfo = CollectionInfo(
//                    rowCount = aisleList.size,
//                    columnCount = 1
//                )
//            }
//        ) {
//            itemsIndexed(aisleList) { index, aisle ->
//                HomeFeedCell(
//                    aisle = aisle,
//                    navigateToDetailScreen = navigateToDetailScreen,
//                    selectAisle = selectAisle,
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
// * A composable that displays a single post in the home feed.
// *
// * @param aisle The post to display.
// */
//@Composable
//private fun HomeFeedCell(
//    aisle: Aisle,
//    navigateToDetailScreen: () -> Unit,
//    selectAisle: (Aisle) -> Unit,
//    modifier: Modifier = Modifier
//) {
//    ElevatedCard(
//        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp),
//        modifier = modifier
//            .fillMaxWidth()
//            .height(80.dp),
//        onClick = {
//            selectAisle(aisle)
//            navigateToDetailScreen()
//        }
//    ) {
//        TextTitleMedium(text = aisle.name)
//    }
//}
//
//@Composable
//fun ErrorScreen(
//    modifier: Modifier = Modifier,
//    contentPadding: PaddingValues,
//    loadData: () -> Unit
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
//            onClick = loadData,
//            shape = MaterialTheme.shapes.extraSmall,
//            colors = ButtonDefaults.buttonColors(containerColor = Red40),
//            textColor = White
//        )
//    }
//}