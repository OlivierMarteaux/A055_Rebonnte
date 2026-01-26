package com.oliviermarteaux.a055_rebonnte.ui.navigation

//import androidx.annotation.StringRes
//import androidx.compose.ui.Modifier
//import androidx.navigation.NavGraphBuilder
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.composable
//import androidx.navigation.navigation
//import com.oliviermarteaux.shared.firebase.authentication.ui.screen.login.LoginScreen
//import com.oliviermarteaux.shared.firebase.authentication.ui.screen.password.PasswordScreen
//import com.oliviermarteaux.shared.firebase.authentication.ui.screen.reset.ResetScreen
//import com.oliviermarteaux.shared.firebase.authentication.ui.screen.splash.SplashScreen
//import com.oliviermarteaux.shared.navigation.Screen
//
//fun NavGraphBuilder.authNavGraph(
//    navHostController: NavHostController,
//    logoRes: Int,
//    imageModifier: Modifier,
//    @StringRes serverClientIdStringRes: Int = -1,
//    navigateToHomeScreen: () -> Unit
//) {
//    navigation(
//        startDestination = Screen.Splash.route,
//        route = SharedNavGraph.AUTH
//    ) {
//        /*_ SPLASH SCREEN ########################################################################*/
//        composable(route = Screen.Splash.route) {
//            SplashScreen(
//                logoDrawableRes = logoRes,
//                imageModifier = imageModifier,
//                serverClientIdStringRes = serverClientIdStringRes,
//                navigateToLoginScreen = { navHostController.navigate(Screen.Login.route) },
//                navigateToHomeScreen = navigateToHomeScreen
//            )
//        }
//        /*_ LOGIN SCREEN #########################################################################*/
//        composable(route = Screen.Login.route) {
//            LoginScreen(
//                logoDrawableRes = logoRes,
//                imageModifier = imageModifier,
//                onBackClick = { navHostController.navigateUp() },
//                navigateToPasswordScreen = {
//                        email -> navHostController.navigate("password/$email")
//                },
//                navigateToHomeScreen = { navHostController.navigate(Screen.Home.route) }
//            )
//        }
//        /*_ PASSWORD SCREEN ######################################################################*/
//        composable(
//            route = Screen.Password.routeWithArgs,
//            arguments = Screen.Password.navArguments
//        ) {
//            PasswordScreen(
//                logoDrawableRes = logoRes,
//                imageModifier = imageModifier,
//                onBackClick = { navHostController.navigateUp() },
//                navigateToHomeScreen = navigateToHomeScreen,
//                navigateToPasswordResetScreen = {
//                        email -> navHostController.navigate(Screen.Reset.route + "/${email}")
//                }
//            )
//        }
//        /*_ RESET SCREEN #########################################################################*/
//        composable(
//            route = Screen.Reset.routeWithArgs,
//            arguments = Screen.Reset.navArguments,
//        ) {
//            ResetScreen(
//                onBackClick = { navHostController.navigateUp() },
//                navigateToLoginScreen = { navHostController.navigate(Screen.Login.route) },
//                logoDrawableRes = logoRes,
//                imageModifier = imageModifier,
//            )
//        }
//    }
//}