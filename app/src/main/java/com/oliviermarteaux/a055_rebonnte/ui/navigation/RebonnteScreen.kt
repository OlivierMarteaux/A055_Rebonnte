package com.oliviermarteaux.a055_rebonnte.ui.navigation

import androidx.navigation.NamedNavArgument
import com.oliviermarteaux.a055_rebonnte.R
import com.oliviermarteaux.shared.navigation.Screen

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
    data object Home : RebonnteScreen(
        route = "home",
        titleRes = R.string.aisle
    )
    data object AddAisle : RebonnteScreen(
        route = "add_aisle",
        titleRes = R.string.add_a_new_aisle
    )
    data object MedicineList : RebonnteScreen(
        route = "medicine_list",
        titleRes = R.string.medicine_list
    )
    data object AddOrEditMedicine : RebonnteScreen(
        route = "add_or_edit_medicine",
        titleRes = R.string.medicine_list
    )
}