package com.oliviermarteaux.a055_rebonnte.ui.screen.addOrEditMedicine

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.oliviermarteaux.a055_rebonnte.R
import com.oliviermarteaux.a055_rebonnte.domain.model.Aisle
import com.oliviermarteaux.a055_rebonnte.domain.model.Medicine
import com.oliviermarteaux.a055_rebonnte.domain.model.MedicineChange
import com.oliviermarteaux.a055_rebonnte.ui.composable.RebonnteItemList
import com.oliviermarteaux.a055_rebonnte.ui.composable.RebonnteSaveButton
import com.oliviermarteaux.a055_rebonnte.ui.screen.MedicineViewModel
import com.oliviermarteaux.a055_rebonnte.ui.screen.home.HomeViewModel
import com.oliviermarteaux.localshared.composables.SharedFilledIntTextField
import com.oliviermarteaux.localshared.composables.SharedFilledItemTextField
import com.oliviermarteaux.shared.composables.CenteredCircularProgressIndicator
import com.oliviermarteaux.shared.composables.SharedFilledTextField
import com.oliviermarteaux.shared.composables.SharedScaffold
import com.oliviermarteaux.shared.composables.SharedToast
import com.oliviermarteaux.shared.composables.spacer.SpacerLarge
import com.oliviermarteaux.shared.composables.spacer.SpacerMedium
import com.oliviermarteaux.shared.composables.texts.TextTitleLarge
import com.oliviermarteaux.shared.ui.UiState
import com.oliviermarteaux.shared.ui.theme.SharedPadding
import com.oliviermarteaux.shared.ui.theme.ToastPadding
import com.oliviermarteaux.shared.compose.R as oR


@Composable
fun AddOrEditMedicineScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    medicineViewModel: MedicineViewModel,
    navigateBack: () -> Unit,
) {

    val cdItem = stringResource(R.string.medicine)
    val cdCreationTitle = stringResource(R.string.add_a_new, cdItem)
    val cdCreation = stringResource(
        R.string.creation_of_a_new_fill_in_the_fields_and_validate_to_create_a_new,
        cdItem,
        cdItem
    )
    val cdEditTitle = stringResource(R.string.edit, medicineViewModel.medicine.name)
    val cdEdit = stringResource(
        R.string.edit_the_fill_in_the_fields_and_validate_to_edit_it,
        medicineViewModel.medicine.name,
        cdItem
    )

    with(medicineViewModel) {
        SharedScaffold(
            title = if (medicineCreation) cdCreationTitle else cdEditTitle,
            screenContentDescription = if (medicineCreation) cdCreation else cdEdit,
            onBackClick = navigateBack
        ) { paddingValues ->

            Box {
                AddScreenBody(
                    medicine = medicine,
                    sourceMedicine = sourceMedicine,
                    modifier = Modifier.testTag("AddOrEditMedicineScreen"),
                    updateMedicineName = ::updateMedicineName,
                    addMedicine = { addMedicine {
                        navigateBack()
                    }},
                    updateMedicine = { updateMedicine { navigateBack() } },
                    paddingValues = paddingValues,
                    updateMedicineStock = ::updateMedicineStock,
                    updateMedicineAisle = ::updateMedicineAisle,
                    homeViewModel = homeViewModel,
                    medicineCreation = medicineCreation,
                )
                when {
                    addOrEditMedicineUiState is UiState.Loading -> {
                        CenteredCircularProgressIndicator()
                    }

                    networkError -> SharedToast(
                        text = stringResource(oR.string.network_error_check_your_internet_connection),
                        bottomPadding = ToastPadding.medium
                    )

                    unknownError -> SharedToast(
                        text = stringResource(oR.string.an_unknown_error_occurred),
                        bottomPadding = ToastPadding.medium
                    )
                }
            }
        }
    }
}

@Composable
fun AddScreenBody(
    medicine: Medicine,
    sourceMedicine: Medicine,
    modifier: Modifier = Modifier,
    updateMedicineName: (String) -> Unit,
    addMedicine: () -> Unit,
    updateMedicine: () -> Unit,
    paddingValues: PaddingValues,
    updateMedicineStock: (Int) -> Unit,
    updateMedicineAisle: (Aisle) -> Unit,
    homeViewModel: HomeViewModel,
    medicineCreation: Boolean,
) {
    val configuration = LocalConfiguration.current
    val orientation = configuration.orientation
    val isLandscape = orientation == Configuration.ORIENTATION_LANDSCAPE

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(bottom = SharedPadding.xxl)
            .padding(horizontal = SharedPadding.large)
            .let { if (isLandscape) it.verticalScroll(rememberScrollState()) else it},
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AddScreenTextForm(
            medicine = medicine,
            updateMedicineName = updateMedicineName,
            updateMedicineStock = updateMedicineStock,
            updateMedicineAisle = updateMedicineAisle,
            homeViewModel = homeViewModel,
            medicineCreation = medicineCreation,
            isStockError = medicine.stock.toString().isEmpty()
                    && if (!medicineCreation) medicine.stock != sourceMedicine.stock else true
        )

        RebonnteSaveButton(
            onClick = {
                when (medicineCreation) {
                    true -> {
                        Log.d("OM_TAG", "AddScreenBody::AddScreenSaveButton: AddMedicine()")
                        addMedicine()
                    }
                    false -> {
                        Log.d("OM_TAG", "AddScreenBody::AddScreenSaveButton: UpdateMedicine()")
                        updateMedicine()
                    }
                }
            },
            enabled = (
                medicine.name.isNotEmpty()
                        && medicine.aisle.name.isNotEmpty()
                        && medicine.stock.toString().isNotEmpty()
                        && if (!medicineCreation) medicine.stock != sourceMedicine.stock else true
                    )
        )
        SpacerLarge()
        TextTitleLarge(text = stringResource(R.string.change_record))
        SpacerMedium()

        with(medicine) {
            RebonnteItemList(
                itemList = changeRecord,
                getItemTitle = MedicineChange::getTitle,
                itemText = MedicineChange::getDescription
            )
        }
    }
}

@Composable
fun AddScreenTextForm(
    medicine:Medicine,
    updateMedicineName: (String) -> Unit,
    updateMedicineStock: (Int) -> Unit,
    updateMedicineAisle: (Aisle) -> Unit,
    homeViewModel: HomeViewModel,
    medicineCreation: Boolean,
    isStockError: Boolean
){
    with(medicine) {
        //_ Medicine name
        SharedFilledTextField(
            value = name,
            onValueChange = { updateMedicineName(it) },
            label = stringResource(oR.string.name),
            textFieldModifier = Modifier.fillMaxWidth(),
            isError = name.isEmpty(),
            errorText = stringResource(R.string.please_enter_a_name),
            bottomPadding = SharedPadding.large,
            enabled = medicineCreation
        )

        //_ Medicine aisle

        val cdMedicineAisle =
            stringResource(R.string.medicine_aisle_picker_double_tap_to_select_an_aisle)

        SharedFilledItemTextField (
            value = aisle.name,
            itemList = homeViewModel.aisleList,
            selectedItem = aisle,
            itemLabel = {aisle -> aisle.name},
            label = stringResource(R.string.aisle),
            textFieldModifier = Modifier.fillMaxWidth(),
            isError = aisle.name.isEmpty(),
            errorText = stringResource(R.string.please_enter_a_name),
            bottomPadding = SharedPadding.large,
            enabled = medicineCreation,
            contentDescription = cdMedicineAisle
        ) { updateMedicineAisle(it) }

        //_ Medicine stock

        val cdMedicineStock =
            stringResource(R.string.medicine_stock_picker_double_tap_to_select_a_quantity_in_stock)

        SharedFilledIntTextField(
            value = stock,
            onConfirm = { updateMedicineStock(it) },
            label = stringResource(R.string.stock_label),
            textFieldModifier = Modifier.fillMaxWidth(),
            isError = isStockError,
            errorText = stringResource(R.string.please_enter_a_valid_stock),
            bottomPadding = SharedPadding.large,
            contentDescription = cdMedicineStock
        )
    }
}