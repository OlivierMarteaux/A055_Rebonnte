package com.oliviermarteaux.a055_rebonnte.ui.composable

import android.util.Log
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
import com.oliviermarteaux.shared.composables.CenteredCircularProgressIndicator
import com.oliviermarteaux.shared.composables.SharedToast
import com.oliviermarteaux.shared.ui.ListUiState
import com.oliviermarteaux.shared.ui.UiState
import com.oliviermarteaux.shared.ui.theme.SharedPadding
import com.oliviermarteaux.shared.ui.theme.ToastPadding
import com.oliviermarteaux.shared.compose.R
import com.oliviermarteaux.shared.firebase.authentication.ui.AuthUserViewModel
import com.oliviermarteaux.shared.utils.CrudAction

@Composable
fun <T> RebonnteItemListBody(
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    testTag: String = "MedicineListScreen",
    listUiState: ListUiState<T>,
    actionUiState: UiState<Unit>,
    resetUiState: () -> Unit = {},
    itemCrudAction: CrudAction = CrudAction.NONE,
    resetItemCrudAction: (() -> Unit)? = {},
    listViewModel: AuthUserViewModel,
    itemList: List<T>,
    itemLabel: String,
    item: T,
    itemId: (T) -> String,
    itemTitle: (T) -> String,
    itemText: @Composable (T) -> String = { "" },
    onSearchFocusRequester: FocusRequester = FocusRequester(),
    reloadItemList: () -> Unit,
    showFab: () -> Unit = {},
    hideFab: () -> Unit = {},
    isLastPage: Boolean,
    loadNextPage: () -> Unit,
    itemModifier: Modifier = Modifier,
    lazyListModifier: Modifier = Modifier,
    //_ trailing lambda !
    onItemClick: (T) -> Unit
){
    with(listViewModel) {
        Box(
            modifier = Modifier.testTag(testTag)
        ) {
            //_ UiState management: Empty, Error, Loading, Success
            val cdLoadingState =
                stringResource(R.string.please_wait_server_connection_in_progress)
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
                    SharedToast("No $itemLabel available")
                }

                is ListUiState.Error -> {
                    hideFab()
                    RebonnteErrorScreen(
                        modifier = modifier,
                        contentPadding = contentPadding,
                        loadItems = reloadItemList
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
                        itemId = itemId,
                        itemTitle = itemTitle,
                        itemText = itemText,
                        onItemClick = onItemClick,
                        isLastPage = isLastPage,
                        loadNextPage = loadNextPage,
                        itemModifier = itemModifier,
                        lazyListModifier = lazyListModifier
                    )
                }
            }
            when {
                authError -> SharedToast(
                    text = stringResource(R.string.an_account_is_mandatory_to_add_or_edit_a, itemLabel),
                    bottomPadding = ToastPadding.high
                )
                networkError -> SharedToast (
                    text = stringResource(R.string.network_error_check_your_internet_connection),
                    bottomPadding = ToastPadding.veryHigh
                )
                successfulItemCreation -> {
                    Log.d("OM_TAG", "RebonnteListBody: successful item creation")
                    SharedToast(
                        text = stringResource(
                            R.string.successfully_created,
                            itemLabel,
                            itemTitle(item)
                        ),
                        bottomPadding = ToastPadding.high
                    )
                }
                successfulItemUpdate -> {
                    Log.d("OM_TAG", "RebonnteListBody: successful item update")
                    SharedToast(
                        text = stringResource(
                            R.string.successfully_edited,
                            itemLabel,
                            itemTitle(item)
                        ),
                        bottomPadding = ToastPadding.high
                    )
                }
                successfulItemDeletion -> {
                    Log.d("OM_TAG", "RebonnteListBody: successful item deletion")
                    SharedToast(
                        text = stringResource(
                            R.string.successfully_deleted,
                            itemLabel,
                            itemTitle(item)
                        ),
                        bottomPadding = ToastPadding.high
                    )
                }
            }
            if (actionUiState is UiState.Success) {
                Log.d("OM_TAG", "RebonnteListBody: actionUiState is success")
                when (itemCrudAction) {
                    CrudAction.ADD -> showSuccessfulItemCreationToast()
                    CrudAction.UPDATE -> showSuccessfulItemUpdateToast()
                    CrudAction.DELETE -> showSuccessfulItemDeletionToast()
                    else -> {Log.d("OM_TAG", "RebonnteListBody: no CrudAction set")}
                }
                resetItemCrudAction?.invoke()
                resetUiState()
                reloadItemList()
            } else { Log.d("OM_TAG", "RebonnteItemListBody: actionUiState is $actionUiState")}
        }
    }
}