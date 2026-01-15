package com.oliviermarteaux.a055_rebonnte.ui.screen.aisleDetail

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.oliviermarteaux.a055_rebonnte.R
import com.oliviermarteaux.a055_rebonnte.domain.model.Medicine
import com.oliviermarteaux.a055_rebonnte.ui.composable.RebonnteItemListBody
import com.oliviermarteaux.a055_rebonnte.ui.navigation.RebonnteScreen
import com.oliviermarteaux.a055_rebonnte.ui.screen.AisleViewModel
import com.oliviermarteaux.a055_rebonnte.ui.screen.MedicineViewModel
import com.oliviermarteaux.a055_rebonnte.ui.screen.MedicineListViewModel
import com.oliviermarteaux.shared.composables.SharedScaffold
import com.oliviermarteaux.shared.ui.theme.SharedPadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AisleDetailScreen(
    modifier: Modifier = Modifier,
    aisleViewModel: AisleViewModel,
    medicineListViewModel: MedicineListViewModel,
    medicineViewModel: MedicineViewModel,
    navigateBack: () -> Unit = {},
    navigateToAddOrEditMedicineScreen: () -> Unit
) {
    val cdScreenTitle = stringResource(RebonnteScreen.AisleDetail.titleRes)
    val cdContainer = stringResource(R.string.aisle)
    val cdItems = stringResource(R.string.medicines)
    val cdScreen = "You are on the $cdScreenTitle screen. Here you can browse all the $cdItems in this $cdContainer."

    SharedScaffold(
        title = stringResource(RebonnteScreen.AisleDetail.titleRes),
        screenContentDescription = cdScreen,
        onBackClick = navigateBack,
        // top app bar
        topAppBarModifier = Modifier.padding(horizontal = SharedPadding.small),
    ) { contentPadding ->
        with(medicineListViewModel) {
            LaunchedEffect(medicineListUiState) {
                Log.i(
                    "OM_TAG",
                    "MedicineListViewModel: LaunchedEffect: medicineListUiState = $medicineListUiState"
                )
            }
            with(aisleViewModel) {
                with(medicineViewModel) {
                    RebonnteItemListBody(
                        contentPadding = contentPadding,
                        modifier = modifier,
                        testTag = "AisleDetailScreen",
                        listUiState = medicineListUiState,
                        listViewModel = medicineListViewModel,
                        itemLabel = stringResource(R.string.medicine),
                        itemList = medicineList.filter { it.aisle == aisle },
                        itemTitle = Medicine::name ,
                        itemText = { medicine: Medicine ->
                            stringResource(R.string.stock, medicine.stock)
                                   },
                        reloadItemOnError = ::loadMedicines,
                    ) { medicine ->
                        selectMedicine(medicine)
                        switchToMedicineEditionMode()
                        navigateToAddOrEditMedicineScreen()
                    }
                }
            }
        }
    }
}