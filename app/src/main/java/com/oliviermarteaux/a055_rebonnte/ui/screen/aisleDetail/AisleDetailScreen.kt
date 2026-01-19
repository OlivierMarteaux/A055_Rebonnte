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
import com.oliviermarteaux.a055_rebonnte.ui.screen.CrudAction
import com.oliviermarteaux.a055_rebonnte.ui.screen.MedicineViewModel
import com.oliviermarteaux.a055_rebonnte.ui.screen.MedicineListViewModel
import com.oliviermarteaux.localshared.composables.SharedScaffold
import com.oliviermarteaux.shared.ui.UiState
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
    with(medicineViewModel) {
        val cdScreenTitle = stringResource(RebonnteScreen.AisleDetail.titleRes)
        val cdContainer = stringResource(R.string.aisle)
        val cdItem = stringResource(R.string.medicine)
        val cdItems = stringResource(R.string.medicines)
        val cdItemAction: String = run {
            resetAddOrEditMedicineUiState()
            when (medicineCrudAction) {
                CrudAction.ADD -> stringResource(R.string.successfully_created, cdItem, medicine.name)
                CrudAction.UPDATE -> stringResource(R.string.successfully_edited, cdItem, medicine.name)
                CrudAction.DELETE -> stringResource(R.string.successfully_deleted, cdItem, medicine.name)
                else -> ""
            }
        }
        val cdScreen = stringResource(
            R.string.you_are_on_the_screen_here_you_can_browse_all_the_in_this,
            cdScreenTitle,
            cdItems,
            cdContainer
        )

        SharedScaffold(
            title = stringResource(RebonnteScreen.AisleDetail.titleRes),
            screenContentDescription = cdScreen,
            onBackClick = navigateBack,
            // top app bar
            topAppBarModifier = Modifier.padding(horizontal = SharedPadding.small),
            //_ semantic state
            semanticState = addOrEditMedicineUiState is UiState.Success,
            semanticStateText = cdItemAction
        ) { contentPadding ->
            with(medicineListViewModel) {
                LaunchedEffect(medicineListUiState) {
                    Log.i(
                        "OM_TAG",
                        "MedicineListViewModel: LaunchedEffect: medicineListUiState = $medicineListUiState"
                    )
                }
                with(aisleViewModel) {

                    RebonnteItemListBody(
                        contentPadding = contentPadding,
                        modifier = modifier,
                        testTag = "AisleDetailScreen",
                        listUiState = medicineListUiState,
                        listViewModel = medicineListViewModel,
                        itemLabel = stringResource(R.string.medicine),
                        itemList = medicineList.filter { it.aisle == aisle },
                        item = medicine,
                        itemTitle = Medicine::name ,
                        itemText = { medicine: Medicine ->
                            stringResource(R.string.stock, medicine.stock)
                                   },
                        reloadItemOnError = ::loadFirstPage,
                        actionUiState = addOrEditMedicineUiState,
                        itemCrudAction = medicineCrudAction,
                        resetItemCrudAction = ::resetMedicineCrudAction,
                        resetUiState = ::resetAddOrEditMedicineUiState,
                        isLastPage = isLastPage,
                        loadNextPage = ::loadNextPage

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