package com.oliviermarteaux.a055_rebonnte.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.oliviermarteaux.a055_rebonnte.R

sealed class RebonnteBottomNavItem(
    val screen: RebonnteScreen,
    val icon: ImageVector,
    @param: StringRes val titleRes: Int
) {
    object AisleNavItem: RebonnteBottomNavItem(
        screen = RebonnteScreen.Home,
        icon = Icons.Filled.Home,
        titleRes = R.string.aisles)

    object MedicineNavItem: RebonnteBottomNavItem(
        screen = RebonnteScreen.MedicineList,
        icon = Icons.AutoMirrored.Filled.List,
        titleRes = R.string.medicines)
}