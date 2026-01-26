package com.oliviermarteaux.a055_rebonnte.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.oliviermarteaux.a055_rebonnte.R
import com.oliviermarteaux.a055_rebonnte.domain.model.Aisle
import com.oliviermarteaux.a055_rebonnte.ui.screen.AddAisleScreen
import com.oliviermarteaux.a055_rebonnte.ui.screen.AddOrEditMedicineScreen
import com.oliviermarteaux.a055_rebonnte.ui.screen.AisleDetailScreen
import com.oliviermarteaux.a055_rebonnte.ui.screen.AisleListScreen
import com.oliviermarteaux.a055_rebonnte.ui.screen.MedicineListScreen
import com.oliviermarteaux.a055_rebonnte.ui.viewModel.AisleListViewModel
import com.oliviermarteaux.a055_rebonnte.ui.viewModel.AisleViewModel
import com.oliviermarteaux.a055_rebonnte.ui.viewModel.MedicineListViewModel
import com.oliviermarteaux.a055_rebonnte.ui.viewModel.MedicineViewModel
import com.oliviermarteaux.shared.ui.theme.SharedShapes

@Composable
fun RootNavGraph(
    navHostController: NavHostController,
    logoRes: Int = -1,
    startDestination: String = SharedNavGraph.AUTH
) {
    NavHost(
        navController = navHostController,
        startDestination = startDestination
    ) {
        //_ shared authentication SharedNavGraph
        authNavGraph(
            navHostController = navHostController,
            logoRes = logoRes,
            imageModifier = Modifier.clip(shape = SharedShapes.medium),
            serverClientIdStringRes = R.string.default_web_client_id,
            navigateToHomeScreen = {
                navHostController.navigate(SharedNavGraph.APP) {
                    popUpTo(SharedNavGraph.AUTH) { inclusive = true }
                }
            }
        )

        navigation(
            startDestination = RebonnteScreen.AisleList.route,
            route = SharedNavGraph.APP
        ) {
            /*_ AISLE LIST SCREEN ##############################################################################*/
            composable(route = RebonnteScreen.AisleList.route) { backStackEntry ->

                val parentEntry = remember(backStackEntry) {
                    navHostController.getBackStackEntry(SharedNavGraph.APP)
                }

                val aisleListViewModel: AisleListViewModel = hiltViewModel(parentEntry)
                val aisleViewModel: AisleViewModel = hiltViewModel(parentEntry)
                val medicineListViewModel: MedicineListViewModel = hiltViewModel(parentEntry)
                val medicineViewModel: MedicineViewModel = hiltViewModel(parentEntry)

                AisleListScreen(
                    aisleListViewModel = aisleListViewModel,
                    aisleViewModel = aisleViewModel,
                    medicineListViewModel = medicineListViewModel,
                    navController = navHostController,
                    navigateToDetailScreen = {navHostController.navigate(RebonnteScreen.AisleDetail.route) },
                    navigateToAddScreen = { navHostController.navigate(RebonnteScreen.AddAisle.route) }
                )
            }
            /*_ AISLE DETAIL SCREEN ##############################################################################*/
            composable(route = RebonnteScreen.AisleDetail.route) {backStackEntry ->

                val parentEntry = remember(backStackEntry) {
                    navHostController.getBackStackEntry(SharedNavGraph.APP)
                }

                val aisleListViewModel: AisleListViewModel = hiltViewModel(parentEntry)
                val aisleViewModel: AisleViewModel = hiltViewModel(parentEntry)
                val medicineListViewModel: MedicineListViewModel = hiltViewModel(parentEntry)
                val medicineViewModel: MedicineViewModel = hiltViewModel(parentEntry)

                AisleDetailScreen(
                    medicineViewModel = medicineViewModel,
                    medicineListViewModel = medicineListViewModel,
                    aisleViewModel = aisleViewModel,
                    navigateToAddOrEditMedicineScreen = {
                        navHostController.navigate(RebonnteScreen.AddOrEditMedicine.route)
                    },
                    navigateBack = { navHostController.navigateUp() },
                )
            }
            /*_ MEDICINE LIST SCREEN ##############################################################################*/
            composable(route = RebonnteScreen.MedicineList.route) { backStackEntry ->

                val parentEntry = remember(backStackEntry) {
                    navHostController.getBackStackEntry(SharedNavGraph.APP)
                }

                val aisleListViewModel: AisleListViewModel = hiltViewModel(parentEntry)
                val aisleViewModel: AisleViewModel = hiltViewModel(parentEntry)
                val medicineListViewModel: MedicineListViewModel = hiltViewModel(parentEntry)
                val medicineViewModel: MedicineViewModel = hiltViewModel(parentEntry)

                MedicineListScreen(
                    medicineViewModel = medicineViewModel,
                    medicineListViewModel = medicineListViewModel,
                    aisleListViewModel = aisleListViewModel,
                    navController = navHostController,
                    navigateToAddOrEditMedicineScreen = {
                        aisleViewModel.selectAisle(Aisle())
                        navHostController.navigate(RebonnteScreen.AddOrEditMedicine.route)
                    },
                )
            }
            /*_ ADD AISLE SCREEN ##########################################################################*/
            composable(route = RebonnteScreen.AddAisle.route) { backStackEntry ->

                val parentEntry = remember(backStackEntry) {
                    navHostController.getBackStackEntry(SharedNavGraph.APP)
                }

                val aisleListViewModel: AisleListViewModel = hiltViewModel(parentEntry)
                val aisleViewModel: AisleViewModel = hiltViewModel(parentEntry)
                val medicineListViewModel: MedicineListViewModel = hiltViewModel(parentEntry)
                val medicineViewModel: MedicineViewModel = hiltViewModel(parentEntry)

                AddAisleScreen(
                    navigateBack = { navHostController.navigateUp() },
                    aisleViewModel = aisleViewModel,
                    aisleListViewModel = aisleListViewModel
                )
            }
            /*_ ADD OR EDIT MEDICINE SCREEN ##########################################################################*/
            composable(route = RebonnteScreen.AddOrEditMedicine.route) { backStackEntry ->

                val parentEntry = remember(backStackEntry) {
                    navHostController.getBackStackEntry(SharedNavGraph.APP)
                }

                val aisleListViewModel: AisleListViewModel = hiltViewModel(parentEntry)
                val aisleViewModel: AisleViewModel = hiltViewModel(parentEntry)
                val medicineListViewModel: MedicineListViewModel = hiltViewModel(parentEntry)
                val medicineViewModel: MedicineViewModel = hiltViewModel(parentEntry)

                AddOrEditMedicineScreen(
                    medicineViewModel = medicineViewModel,
                    aisleListViewModel = aisleListViewModel,
                    medicineListViewModel = medicineListViewModel,
                    aisleViewModel = aisleViewModel,
                    navigateBack = { navHostController.navigateUp() },
                )
            }
        }
    }
}
