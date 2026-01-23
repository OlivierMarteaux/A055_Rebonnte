package com.oliviermarteaux.a055_rebonnte.ui.composable

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.oliviermarteaux.a055_rebonnte.ui.navigation.RebonnteBottomNavItem
import com.oliviermarteaux.shared.composables.IconSource
import com.oliviermarteaux.shared.composables.SharedIcon
import com.oliviermarteaux.shared.composables.extensions.cdButtonSemantics
import com.oliviermarteaux.shared.compose.R

@Composable
fun RebonnteBottomAppBar(
    navController: NavController,
    item1: RebonnteBottomNavItem,
    callback1: () -> Unit = {},
    item2: RebonnteBottomNavItem,
    callback2: () -> Unit = {},
) {
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        SharedNavBarItem(
            item = item1,
            navController = navController,
            currentRoute = currentRoute,
            callback = callback1
        )
        SharedNavBarItem(
            item = item2,
            navController = navController,
            currentRoute = currentRoute,
            callback = callback2
        )
    }
}

@Composable
fun RowScope.SharedNavBarItem(
    navController: NavController,
    currentRoute: String?,
    item: RebonnteBottomNavItem,
    callback: () -> Unit = {}
){
    val itemTitle = stringResource(item.titleRes)
    val currentItem = if(currentRoute == item.screen.route) stringResource(R.string.you_are_on_this_screen) else ""
    val routeItem = if (currentRoute == item.screen.route) "" else stringResource(
        R.string.double_tap_to_navigate_to_screen,
        itemTitle
    )
    val cdItem = itemTitle + routeItem + currentItem
    NavigationBarItem(
        icon = { SharedIcon(IconSource.VectorIcon(item.icon)) },
        modifier = Modifier.cdButtonSemantics(cdItem),
        label = { Text(itemTitle) },
        selected = currentRoute == item.screen.route,
        onClick =
            {
                callback()
                navController.navigate(item.screen.route) {
                    navController.graph.startDestinationRoute?.let { route ->
                        popUpTo(route) {
                            saveState = true
                        }
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
    )
}

//        items.forEach { item ->
//            val itemTitle = stringResource(item.titleRes)
//            val currentItem = if(currentRoute == item.screen.route) stringResource(R.string.you_are_on_this_screen) else ""
//            val routeItem = if (currentRoute == item.screen.route) "" else stringResource(
//                R.string.double_tap_to_navigate_to_screen,
//                itemTitle
//            )
//            val cdItem = itemTitle + routeItem + currentItem
//            NavigationBarItem(
//                icon = { SharedIcon(IconSource.VectorIcon(item.icon)) },
//                modifier = Modifier.cdButtonSemantics(cdItem),
//                label = { Text(itemTitle) },
//                selected = currentRoute == item.screen.route,
//                onClick = {
//                    navController.navigate(item.screen.route) {
//                        navController.graph.startDestinationRoute?.let { route ->
//                            popUpTo(route) {
//                                saveState = true
//                            }
//                        }
//                        launchSingleTop = true
//                        restoreState = true
//                    }
//                }
//            )
//        }