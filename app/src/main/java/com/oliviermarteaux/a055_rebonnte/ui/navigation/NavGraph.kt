package com.oliviermarteaux.a055_rebonnte.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.oliviermarteaux.a055_rebonnte.R
import com.oliviermarteaux.a055_rebonnte.ui.screen.AisleViewModel
import com.oliviermarteaux.a055_rebonnte.ui.screen.MedicineViewModel
import com.oliviermarteaux.a055_rebonnte.ui.screen.account.AccountScreen
import com.oliviermarteaux.a055_rebonnte.ui.screen.addAisle.AddAisleScreen
import com.oliviermarteaux.a055_rebonnte.ui.screen.addOrEditMedicine.AddOrEditMedicineScreen
import com.oliviermarteaux.a055_rebonnte.ui.screen.home.HomeScreen
import com.oliviermarteaux.a055_rebonnte.ui.screen.medicineList.MedicineListScreen
import com.oliviermarteaux.shared.cameraX.CameraScreen
import com.oliviermarteaux.shared.firebase.authentication.ui.screen.login.LoginScreen
import com.oliviermarteaux.shared.firebase.authentication.ui.screen.password.PasswordScreen
import com.oliviermarteaux.shared.firebase.authentication.ui.screen.reset.ResetScreen
import com.oliviermarteaux.shared.firebase.authentication.ui.screen.splash.SplashScreen
import com.oliviermarteaux.shared.navigation.Screen

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
    medicineViewModel: MedicineViewModel = hiltViewModel()
){
    NavHost(
        navController = navHostController,
        startDestination = startDestination
    ) {
        /*_ SPLASH SCREEN ############################################################################*/
        composable(route = Screen.Splash.route) {
            SplashScreen(
                logoDrawableRes = logoRes,
                serverClientIdStringRes = R.string.default_web_client_id,
                navigateToLoginScreen = { navHostController.navigate(Screen.Login.route) },
                navigateToHomeScreen = { navHostController.navigate(Screen.Home.route) }
            )
        }
        /*_ LOGIN SCREEN #############################################################################*/
        composable(route = Screen.Login.route) {
            LoginScreen(
                logoDrawableRes = logoRes,
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
                logoDrawableRes = logoRes
            )
        }
        /*_ AISLE SCREEN ##############################################################################*/
        composable(route = RebonnteScreen.Home.route) {
            HomeScreen(
                aisleViewModel = aisleViewModel,
                navController = navHostController,
                navigateToDetailScreen = {navHostController.navigate(Screen.Detail.route) },
                navigateToAddScreen = { navHostController.navigate(RebonnteScreen.AddAisle.route) }
            )
        }
        /*_ MEDICINE LIST SCREEN ##############################################################################*/
        composable(route = RebonnteScreen.MedicineList.route) {
            MedicineListScreen(
                medicineViewModel = medicineViewModel,
                navController = navHostController,
                navigateToAddOrEditMedicineScreen = {
                    navHostController.navigate(RebonnteScreen.AddOrEditMedicine.route)
                },
            )
        }
        /*_ ACCOUNT SCREEN ###########################################################################*/
        composable(route = Screen.Account.route) {
            AccountScreen(
                navController = navHostController
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