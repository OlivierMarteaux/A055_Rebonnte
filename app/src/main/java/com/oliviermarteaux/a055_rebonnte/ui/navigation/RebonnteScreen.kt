package com.oliviermarteaux.a055_rebonnte.ui.navigation

import androidx.navigation.NamedNavArgument
import com.oliviermarteaux.shared.compose.R

/**
 * A sealed class that represents the different screens in the application.
 *
 * @property route The route for the screen.
 * @property navArguments The navigation arguments for the screen.
 */
sealed class RebonnteScreen(
    val route: String,
    val navArguments: List<NamedNavArgument> = emptyList(),
    val routeWithArgs: String = "",
    val titleRes: Int = -1,
) {
    /**
     * The add aisle screen.
     */
    data object AddAisle : RebonnteScreen(
        route = "add_aisle",
        titleRes = R.string.creation_of_an_event,
    )
}