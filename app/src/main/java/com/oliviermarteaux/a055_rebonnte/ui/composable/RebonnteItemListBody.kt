package com.oliviermarteaux.a055_rebonnte.ui.composable

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.oliviermarteaux.a055_rebonnte.R
import com.oliviermarteaux.shared.composables.CenteredCircularProgressIndicator
import com.oliviermarteaux.shared.composables.SharedToast
import com.oliviermarteaux.shared.firebase.authentication.ui.AuthUserViewModel
import com.oliviermarteaux.shared.ui.ListUiState
import com.oliviermarteaux.shared.ui.theme.SharedPadding
import com.oliviermarteaux.shared.ui.theme.ToastPadding
import com.oliviermarteaux.shared.compose.R as oR

@Composable
fun <T> RebonnteItemListBody(
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    testTag: String = "MedicineListScreen",
    listUiState: ListUiState<T>,
    listViewModel: AuthUserViewModel,
    itemList: List<T>,
    itemLabel: String,
    itemTitle: (T) -> String,
    itemText: @Composable (T) -> String = { "" },
    onSearchFocusRequester: FocusRequester = FocusRequester(),
    reloadItemOnError: () -> Unit,
    showFab: () -> Unit = {},
    hideFab: () -> Unit = {},
    //_ trailing lambda !
    onItemClick: (T) -> Unit
){
    with(listViewModel) {
        Box(
            modifier = Modifier.testTag(testTag)
        ) {
            //_ UiState management: Empty, Error, Loading, Success
            val cdLoadingState =
                stringResource(oR.string.please_wait_server_connection_in_progress)
            when (listUiState) {
                is ListUiState.Loading -> {
                    hideFab()
                    CenteredCircularProgressIndicator(
                        modifier = Modifier.semantics(
                            properties = {
                                contentDescription = cdLoadingState
                            }
                        )
                    )
                }

                is ListUiState.Empty -> {
                    showFab()
                    SharedToast("No medicine available")
                }

                is ListUiState.Error -> {
                    hideFab()
                    RebonnteErrorScreen(
                        modifier = modifier,
                        contentPadding = contentPadding,
                        loadItems = reloadItemOnError
                    )
                }

                is ListUiState.Success -> {
                    showFab()
                    RebonnteItemList(
                        modifier = modifier
                            .focusRequester(onSearchFocusRequester)
                            .focusable()
                            .consumeWindowInsets(contentPadding)   // 👈 prevents double padding,
                            .fillMaxWidth()
                            .padding(contentPadding)
                            .padding(horizontal = SharedPadding.large),
                        itemList = itemList,
                        itemTitle = itemTitle,
                        itemText = itemText,
                        onItemClick = onItemClick
                    )
                }
            }
            if (authError) SharedToast(
                text = stringResource(R.string.an_account_is_mandatory_to_add_or_edit_a, itemLabel),
                bottomPadding = ToastPadding.high
            )
            if (networkError) SharedToast(
                text = stringResource(oR.string.network_error_check_your_internet_connection),
                bottomPadding = ToastPadding.veryHigh
            )
        }
    }
}