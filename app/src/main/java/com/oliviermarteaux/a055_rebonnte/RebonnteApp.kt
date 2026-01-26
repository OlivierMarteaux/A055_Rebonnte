package com.oliviermarteaux.a055_rebonnte

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.oliviermarteaux.a055_rebonnte.ui.navigation.RebonnteScreen
import com.oliviermarteaux.a055_rebonnte.ui.navigation.RootNavGraph
import com.oliviermarteaux.a055_rebonnte.ui.navigation.SharedNavGraph
import com.oliviermarteaux.shared.utils.TestConfig
import com.oliviermarteaux.shared.composables.startup.DismissKeyboardOnTapOutside
import com.oliviermarteaux.shared.navigation.LogRoutes
import com.oliviermarteaux.shared.navigation.Screen

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun RebonnteApp(){

    val navController = rememberNavController()

    Log.d("OM_TAG", "BuildConfig: Debug = ${BuildConfig.DEBUG}")

    val startDestination: String =
        if (TestConfig.isTest) {
        Log.d("OM_TAG", "start screen = ${RebonnteScreen.AisleList.route}")
//            RebonnteScreen.AisleList.route
            SharedNavGraph.APP
        } else {
            Log.d("OM_TAG", "start screen = ${Screen.Splash.route}")
//            Screen.Splash.route
            SharedNavGraph.AUTH
        }

//    if (!TestConfig.isTest) {
//        RequestPermissionsOnFirstLaunch(
//            Manifest.permission.CAMERA,
//            Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.POST_NOTIFICATIONS
//        )
//    }

    Surface {
        DismissKeyboardOnTapOutside {
//            SharedNavGraph(
//                navHostController = navController,
//                startDestination = startDestination,
//                logoRes = R.drawable.rebonnte_logo
//            )
            RootNavGraph(
                navHostController = navController,
                startDestination = startDestination,
                logoRes = R.drawable.rebonnte_logo
            )
        }
    }

    LogRoutes(navController)
}