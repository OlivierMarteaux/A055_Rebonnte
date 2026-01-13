package com.oliviermarteaux.a055_rebonnte

import android.Manifest
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.oliviermarteaux.a055_rebonnte.ui.navigation.SharedNavGraph
import com.oliviermarteaux.localshared.utils.TestConfig
import com.oliviermarteaux.shared.composables.startup.DismissKeyboardOnTapOutside
import com.oliviermarteaux.shared.composables.startup.RequestPermissionsOnFirstLaunch
import com.oliviermarteaux.shared.navigation.LogRoutes
import com.oliviermarteaux.shared.navigation.Screen

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun RebonnteApp(){

    val navController = rememberNavController()

    Log.d("OM_TAG", "BuildConfig: Debug = ${BuildConfig.DEBUG}")

    val startDestination: String =
        if (TestConfig.isTest) {
            Log.d("OM_TAG", "start screen = ${Screen.Home.route}")
            Screen.Home.route
        } else {
            Log.d("OM_TAG", "start screen = ${Screen.Splash.route}")
            Screen.Splash.route
        }

    if (!TestConfig.isTest) {
        RequestPermissionsOnFirstLaunch(
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.POST_NOTIFICATIONS
        )
    }

    Surface {
        DismissKeyboardOnTapOutside {
            SharedNavGraph(
                navHostController = navController,
                startDestination = startDestination,
                logoRes = R.drawable.eventorias_logo
            )
        }
    }

    LogRoutes(navController)
}