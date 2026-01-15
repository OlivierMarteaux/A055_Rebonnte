package com.oliviermarteaux.a055_rebonnte.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.oliviermarteaux.a055_rebonnte.R
import com.oliviermarteaux.a055_rebonnte.ui.screen.AisleViewModel
import com.oliviermarteaux.a055_rebonnte.ui.screen.MedicineListViewModel
import com.oliviermarteaux.a055_rebonnte.ui.screen.MedicineViewModel
import com.oliviermarteaux.a055_rebonnte.ui.screen.addAisle.AddAisleScreen
import com.oliviermarteaux.a055_rebonnte.ui.screen.addOrEditMedicine.AddOrEditMedicineScreen
import com.oliviermarteaux.a055_rebonnte.ui.screen.aisleDetail.AisleDetailScreen
import com.oliviermarteaux.a055_rebonnte.ui.screen.home.HomeScreen
import com.oliviermarteaux.a055_rebonnte.ui.screen.medicineList.MedicineListScreen
import com.oliviermarteaux.localshared.composables.LoginScreen
import com.oliviermarteaux.localshared.composables.PasswordScreen
import com.oliviermarteaux.localshared.composables.ResetScreen
import com.oliviermarteaux.localshared.composables.SplashScreen
import com.oliviermarteaux.shared.cameraX.CameraScreen
import com.oliviermarteaux.shared.navigation.Screen
import com.oliviermarteaux.shared.ui.theme.SharedShapes

/**
 * The main navigation graph for the application.
 *
 * @param navHostController The navigation controller for the application.
 */

@Composable
fun SharedNavGraph(
    navHostController: NavHostController,
    startDestination: String,
    logoRes: Int = -1,
    aisleViewModel: AisleViewModel = hiltViewModel(),
    medicineViewModel: MedicineViewModel = hiltViewModel(),
    medicineListViewModel: MedicineListViewModel = hiltViewModel(),
){
    val imageModifier: Modifier = Modifier.clip(shape = SharedShapes.medium)
    NavHost(
        navController = navHostController,
        startDestination = startDestination
    ) {
        /*_ SPLASH SCREEN ############################################################################*/
        composable(route = Screen.Splash.route) {
            SplashScreen(
                logoDrawableRes = logoRes,
                imageModifier = Modifier.clip(shape = RoundedCornerShape(24.dp)),
                serverClientIdStringRes = R.string.default_web_client_id,
                navigateToLoginScreen = { navHostController.navigate(Screen.Login.route) },
                navigateToHomeScreen = { navHostController.navigate(Screen.Home.route) },
            )
        }
        /*_ LOGIN SCREEN #############################################################################*/
        composable(route = Screen.Login.route) {
            LoginScreen(
                logoDrawableRes = logoRes,
                imageModifier = imageModifier,
                onBackClick = { navHostController.navigateUp() },
                navigateToPasswordScreen = {
                        email -> navHostController.navigate("password/$email")
                },
                navigateToHomeScreen = { navHostController.navigate(Screen.Home.route) }
            )
        }
        /*_ PASSWORD SCREEN ##########################################################################*/
        composable(
            route = Screen.Password.routeWithArgs,
            arguments = Screen.Password.navArguments
        ) {
            PasswordScreen(
                logoDrawableRes = logoRes,
                imageModifier = imageModifier,
                onBackClick = { navHostController.navigateUp() },
                navigateToHomeScreen = {
                    navHostController.navigate(Screen.Home.route){
                        popUpTo(0) { inclusive = true } // clear everything
                    }
                },
                navigateToPasswordResetScreen = {
                        email -> navHostController.navigate(Screen.Reset.route + "/${email}")
                }
            )
        }
        /*_ RESET SCREEN #############################################################################*/
        composable(
            route = Screen.Reset.routeWithArgs,
            arguments = Screen.Reset.navArguments,
        ) {
            ResetScreen(
                onBackClick = { navHostController.navigateUp() },
                navigateToLoginScreen = { navHostController.navigate(Screen.Login.route) },
                logoDrawableRes = logoRes,
                imageModifier = imageModifier,
            )
        }
        /*_ AISLE LIST SCREEN ##############################################################################*/
        composable(route = RebonnteScreen.Home.route) {
            HomeScreen(
                aisleViewModel = aisleViewModel,
                navController = navHostController,
                navigateToDetailScreen = {navHostController.navigate(RebonnteScreen.AisleDetail.route) },
                navigateToAddScreen = { navHostController.navigate(RebonnteScreen.AddAisle.route) }
            )
        }
        /*_ AISLE DETAIL SCREEN ##############################################################################*/
        composable(route = RebonnteScreen.AisleDetail.route) {
            AisleDetailScreen(
                aisleViewModel = aisleViewModel,
                medicineViewModel = medicineViewModel,
                medicineListViewModel = medicineListViewModel,
                navigateToAddOrEditMedicineScreen = {
                    navHostController.navigate(RebonnteScreen.AddOrEditMedicine.route)
                },
                navigateBack = { navHostController.navigateUp() },
            )
        }
        /*_ MEDICINE LIST SCREEN ##############################################################################*/
        composable(route = RebonnteScreen.MedicineList.route) {
            MedicineListScreen(
                medicineViewModel = medicineViewModel,
                medicineListViewModel = medicineListViewModel,
                navController = navHostController,
                navigateToAddOrEditMedicineScreen = {
                    navHostController.navigate(RebonnteScreen.AddOrEditMedicine.route)
                },
            )
        }
        /*_ ADD AISLE SCREEN ##########################################################################*/
        composable(route = RebonnteScreen.AddAisle.route) {
            AddAisleScreen(
                navigateBack = { navHostController.navigateUp() },
            )
        }
        /*_ ADD OR EDIT MEDICINE SCREEN ##########################################################################*/
        composable(route = RebonnteScreen.AddOrEditMedicine.route) {
            AddOrEditMedicineScreen(
                medicineViewModel = medicineViewModel,
                navigateBack = { navHostController.navigateUp() },
            )
        }
        /*_ CAMERA SCREEN ##########################################################################*/
        composable(route = Screen.Camera.route) {
            CameraScreen(
                navigateBack = { navHostController.navigateUp() },
            )
        }
    }
}