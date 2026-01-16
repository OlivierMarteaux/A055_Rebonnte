package com.oliviermarteaux.localshared.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.OutlinedTextFieldDefaults.contentPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.hideFromAccessibility
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.oliviermarteaux.shared.composables.IconSource
import com.oliviermarteaux.shared.composables.SharedAsyncImage
import com.oliviermarteaux.shared.composables.SharedIcon
import com.oliviermarteaux.shared.composables.SharedIconButton
import com.oliviermarteaux.shared.composables.SharedSearchBar
import com.oliviermarteaux.shared.composables.accessibility.isTalkBackEnabled
import com.oliviermarteaux.shared.composables.extensions.cdButtonSemantics
import com.oliviermarteaux.shared.composables.texts.TextTitleLarge
import com.oliviermarteaux.shared.composables.texts.TextTitleSmall
import com.oliviermarteaux.shared.compose.R
import com.oliviermarteaux.shared.ui.theme.SharedPadding

/**
 * A reusable scaffold composable with a top app bar, optional floating action button (FAB),
 * and optional menu actions. This composable wraps [Scaffold] from Material 3 and provides
 * a consistent structure for screens across the app.
 *
 * ### Features:
 * - Top app bar with customizable title, optional back button, and menu actions.
 * - Optional FAB with configurable appearance, behavior, and content description.
 * - Supports up to two menu items in a dropdown menu.
 * - Provides content area with [contentPadding] automatically applied.
 *
 * ### Behavior:
 * - If [onBackClick] is provided, a back navigation icon is displayed in the top app bar.
 * - If [onMenuItem1Click] is provided, a menu icon is displayed; optionally supports a second menu item.
 * - If [onFabClick] is provided, a floating action button is displayed with the standard "Add" icon.
 * - Menu and FAB are disabled if their respective callbacks are `null` or [fabEnabled] is `false`.
 *
 * ### Parameters:
 * @param modifier [Modifier] applied to the scaffold.
 * @param title The text displayed as the top app bar title.
 * @param topAppBarModifier [Modifier] applied to the top app bar.
 * @param onFabClick Optional lambda invoked when the FAB is clicked.
 * @param onBackClick Optional lambda invoked when the back button is clicked.
 * @param onMenuItem1Click Optional lambda for the first menu item click.
 * @param onMenuItem2Click Optional lambda for the second menu item click.
 * @param menuItem1Title Title text for the first menu item.
 * @param menuItem2Title Title text for the second menu item.
 * @param fabEnabled Controls whether the FAB is clickable. Defaults to `true`.
 * @param fabShape Shape of the FAB. Defaults to [FloatingActionButtonDefaults.shape].
 * @param fabContainerColor Background color of the FAB. Defaults to [FloatingActionButtonDefaults.containerColor].
 * @param fabContentColor Content color of the FAB. Defaults to [contentColorFor(fabContainerColor)].
 * @param fabInteractionSource Optional [MutableInteractionSource] for the FAB.
 * @param fabContentDescription Accessibility content description for the FAB.
 * @param bottomBar The composable to be used as the bottom app bar.
 * @param content Composable lambda representing the main content of the scaffold. Receives [contentPadding] to account for system bars and FAB.
 *
 * ### Example Usage:
 * ```kotlin
 * @Composable
 * fun ExampleScreen() {
 *     SharedScaffold(
 *         title = "Dashboard",
 *         onBackClick = { /* navigate back */ },
 *         onFabClick = { /* add item */ },
 *         onMenuItem1Click = { /* menu action */ },
 *         menuItem1Title = "Settings",
 *     ) { padding ->
 *         Column(modifier = Modifier.padding(padding)) {
 *             Text("Hello World")
 *         }
 *     }
 * }
 * ```
 *
 * @see Scaffold
 * @see TopAppBar
 * @see FloatingActionButton
 * @see DropdownMenu
 * @see SharedIconButton
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SharedScaffold(
    modifier: Modifier = Modifier,
    //_ topAppBar
    title: String = "",
    screenContentDescription: String = "",
    topAppBarModifier: Modifier = Modifier,
    trailingIcon: IconSource? = null,
    trailingIconAction: (() -> Unit)? = {},
    trailingIconButtonContentDescription: String = "",
    avatarUrl: String? = null,
    onBackClick: (() -> Unit)? = null,
    //_ Semantic state management
    semanticState: Boolean = false,
    semanticStateText: String = "",
    //_ search function
    query: TextFieldValue = TextFieldValue(""),
    searchBarIcon: IconSource = IconSource.VectorIcon(Icons.Default.Clear),
    searchBarIconSemantics: String = "",
    onSearchBarIconClick: () -> Unit = {},
    onQueryChange: ((TextFieldValue) -> Unit)? = null,
    searchBarModifier: Modifier = Modifier,
    searchLabel: String = "",
    searchBarDisplayed: Boolean = false,
    toggleSearchBar: () -> Unit = {},
    onSearch: () -> Unit = {},
    //_ sort function
    onSortByTitleClick: (() -> Unit)? = null,
    onSortByAscendingDateClick: (() -> Unit)? = null,
    onSortByDescendingDateClick: (() -> Unit)? = null,
    onSortByNoneClick: (() -> Unit)? = null,
    onSortByNameClick: (() -> Unit)? = null,
    onSortByAscendingStockClick: (() -> Unit)? = null,
    onSortByDescendingStockClick: (() -> Unit)? = null,
    //_ menu function
    onMenuItem1Click: (() -> Unit)? = null,
    onMenuItem2Click: (() -> Unit)? = null,
    menuItem1Title: String = "",
    menuItem2Title: String = "",
    //_ fab function
    onFabClick: (() -> Unit)? = null,
    fabVisible: Boolean = true,
    fabEnabled: Boolean = true,
    fabShape: Shape =  FloatingActionButtonDefaults.shape,
    fabContainerColor: Color =  FloatingActionButtonDefaults.containerColor,
    fabContentColor: Color = contentColorFor(fabContainerColor),
    fabInteractionSource: MutableInteractionSource? = null,
    fabContentDescription: String = "",
    fabModifier: Modifier = Modifier,
    fabIconTint: Color = contentColorFor(fabContainerColor),
    //_ bottom bar
    bottomBar: @Composable () -> Unit = {},
    //_ content
    content: @Composable (contentPadding: PaddingValues) -> Unit = {},
){
    var menuDisplayed by rememberSaveable { mutableStateOf(false) }
    fun showMenu(){ menuDisplayed = true }
    fun hideMenu(){ menuDisplayed = false }

    var sortOptionsDisplayed by rememberSaveable { mutableStateOf(false) }
    fun showSortOptions(){ sortOptionsDisplayed = true }
    fun hideSortOptions(){ sortOptionsDisplayed = false }

    Scaffold(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    toggleSearchBar()
                })
            }
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        AnimatedVisibility(
                            visible = !searchBarDisplayed,
                            enter = expandHorizontally(),
                            exit = shrinkHorizontally()
                        ) {
                            TextTitleLarge(
                                text = title,
                                modifier = modifier.clearAndSetSemantics(
                                    properties = {
                                        contentDescription = screenContentDescription.ifEmpty { title }
                                    }
                                )
                            )
                        }
                    }
                },
                modifier = topAppBarModifier.height(125.dp),
                navigationIcon = {
                    onBackClick?.let {
                        val cdBackButton = if (semanticState) semanticStateText
                            else stringResource(R.string.back_button_double_tap_to_go_back_to_the_previous_screen)
                        SharedIconButton(
                            icon = IconSource.VectorIcon(Icons.AutoMirrored.Filled.ArrowBack),
                            modifier = Modifier.cdButtonSemantics(cdBackButton)
                        ) { onBackClick() }
                    }
                },
                actions = {
                    avatarUrl?.let {
                        SharedAsyncImage(
                            photoUri = avatarUrl,
                            modifier = Modifier
                                .padding(end = SharedPadding.small)
                                .size(48.dp)
                                .clip(shape = CircleShape)
                                .semantics { hideFromAccessibility() }
                        )
                    }
                    trailingIconAction?.let {
                        SharedIconButton(
                            icon  =  trailingIcon?: IconSource.VectorIcon(Icons.Default.MoreVert),
                            modifier = Modifier
                                .cdButtonSemantics(trailingIconButtonContentDescription)
                                .size(48.dp),
                            onClick = trailingIconAction
                        )
                    }?:trailingIcon?.let{
                        SharedIcon(
                            icon  =  trailingIcon,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(shape = CircleShape)
                        )
                    }
                    onQueryChange?.let {
                        AnimatedVisibility(
                            visible = searchBarDisplayed,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = SharedPadding.small),
//                            enter = expandHorizontally(),
//                            exit = shrinkHorizontally()
                        ) {
                            val searchBarFocusRequester = remember { FocusRequester() }
                            LaunchedEffect(Unit) { searchBarFocusRequester.requestFocus() }
                            val keyboardController = LocalSoftwareKeyboardController.current

                            SharedSearchBar(
                                textFieldValue = query,
                                onQueryChange = onQueryChange,
                                modifier = searchBarModifier
                                    .focusRequester(searchBarFocusRequester)
                                    .fillMaxWidth(),
                                onSearch =  { onSearch(); keyboardController?.hide(); toggleSearchBar() },
                                searchLabel = searchLabel,
                                icon = searchBarIcon,
                                iconSemantics = searchBarIconSemantics,
                                onIconClick = onSearchBarIconClick,
                            )
                        }
                        val cdSearchButton =
                            stringResource(R.string.search_button_double_tap_to_open_the_search_bar)
                        if (!searchBarDisplayed) SharedIconButton(
                            icon = IconSource.VectorIcon(Icons.Default.Search),
                            modifier = Modifier.cdButtonSemantics(cdSearchButton)
                        ) {
                            toggleSearchBar()
                        }
                    }
                    (onSortByTitleClick ?: onSortByNameClick)?.let{
                        val cdSortButton =
                            stringResource(R.string.sort_button_double_tap_to_open_the_sort_menu)
                        SharedIconButton(
                            icon = IconSource.VectorIcon(Icons.Default.SwapVert),
                            modifier = Modifier.cdButtonSemantics(cdSortButton)
                        ){ showSortOptions() }
                        DropdownMenu(
                            expanded = sortOptionsDisplayed,
                            onDismissRequest = { hideSortOptions() }
                        ) {
                            val isTalkBackEnabled = isTalkBackEnabled()
                            val cdSortedByNone =
                                stringResource(R.string.not_sorted_double_tap_to_come_back_to_initial_list_state)
                            val cdAscendingTitle =
                                stringResource(R.string.ascending_title_double_tap_to_sort_by_ascending_title)
                            val cdAscendingName =
                                stringResource(R.string.ascending_name_double_tap_to_sort_by_ascending_name)
                            val cdAscendingStock =
                                stringResource(R.string.ascending_stock_double_tap_to_sort_by_ascending_stock)
                            val cdDescendingStock =
                                stringResource(R.string.descending_stock_double_tap_to_sort_by_descending_stock)
                            val cdAscendingDate =
                                stringResource(R.string.ascending_date_double_tap_to_sort_by_ascending_date)
                            val cdDescendingDate =
                                stringResource(R.string.descending_date_double_tap_to_sort_by_descending_date)
                            val cdCloseSortMenu =
                                stringResource(R.string.close_sort_menu_double_tap_to_close_the_menu)
                            onSortByNoneClick?.let { DropdownMenuItem(
                                text = { TextTitleSmall(text = stringResource(R.string.none)) },
                                modifier = Modifier.cdButtonSemantics(cdSortedByNone),
                                onClick = { onSortByNoneClick() },
                            )}
                            onSortByTitleClick?.let { DropdownMenuItem(
                                text = { TextTitleSmall(text = stringResource(R.string.ascending_title)) },
                                modifier = Modifier.cdButtonSemantics(cdAscendingTitle),
                                onClick = { onSortByTitleClick() },
                            )}
                            onSortByNameClick?.let { DropdownMenuItem(
                                text = { TextTitleSmall(text = stringResource(R.string.name)) },
                                modifier = Modifier.cdButtonSemantics(cdAscendingName),
                                onClick = { onSortByNameClick() },
                            )}
                            onSortByAscendingStockClick?.let { DropdownMenuItem(
                                text = { TextTitleSmall(text = stringResource(R.string.ascending_stock)) },
                                modifier = Modifier.cdButtonSemantics(cdAscendingStock),
                                onClick = { onSortByAscendingStockClick() },
                            )}
                            onSortByDescendingStockClick?.let { DropdownMenuItem(
                                text = { TextTitleSmall(text = stringResource(R.string.descending_stock)) },
                                modifier = Modifier.cdButtonSemantics(cdDescendingStock),
                                onClick = { onSortByDescendingStockClick() },
                            )}
                            onSortByAscendingDateClick?.let { DropdownMenuItem(
                                text = { TextTitleSmall(text = stringResource(R.string.ascending_date)) },
                                modifier = Modifier.cdButtonSemantics(cdAscendingDate),
                                onClick = { onSortByAscendingDateClick() },
                            )}
                            onSortByDescendingDateClick?.let { DropdownMenuItem(
                                text = { TextTitleSmall(text = stringResource(R.string.descending_date)) },
                                modifier = Modifier.cdButtonSemantics(cdDescendingDate),
                                onClick = { onSortByDescendingDateClick() },
                            )}
                            // Show "Close" ONLY for TalkBack users
                            if (isTalkBackEnabled) {
                                DropdownMenuItem(
                                    text = { TextTitleSmall(text = stringResource(R.string.close_menu)) },
                                    modifier = Modifier.cdButtonSemantics(cdCloseSortMenu),
                                    onClick = { hideSortOptions() },
                                )
                            }
                        }
                    }
                    onMenuItem1Click?.let{
                        SharedIconButton(
                            icon = IconSource.VectorIcon(Icons.Default.MoreVert),
                        ) { showMenu() }
                        DropdownMenu(
                            expanded = menuDisplayed,
                            onDismissRequest = { hideMenu() }
                        ) {
                            DropdownMenuItem(
                                text = { TextTitleSmall(text = menuItem1Title) },
                                onClick = {
                                    onMenuItem1Click()
                                    hideMenu()
                                },
                            )
                            onMenuItem2Click?.let {
                                DropdownMenuItem(
                                    text = { TextTitleSmall(text = menuItem2Title) },
                                    onClick = {
                                        onMenuItem2Click()
                                        hideMenu()
                                    },
                                )
                            }
                        }
                    }
                }
            )
        },
        bottomBar = bottomBar,
        floatingActionButton = {
            onFabClick?.let {
                if (fabVisible){
                    FloatingActionButton(
                        onClick = { if (fabEnabled) onFabClick() },
                        modifier = fabModifier
                            .cdButtonSemantics(fabContentDescription),
                        shape = fabShape,
                        containerColor = fabContainerColor,
                        contentColor = fabContentColor,
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(10.dp),
                        interactionSource = fabInteractionSource
                    ) {
                        SharedIcon(
                            icon = IconSource.VectorIcon(Icons.Filled.Add),
                            tint = fabIconTint
                        )
                    }
                }
            }
        },
    ) { contentPadding -> content(contentPadding) }
}