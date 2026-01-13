package com.oliviermarteaux.a055_rebonnte.ui.screen.addOrEditMedicine

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.oliviermarteaux.a055_rebonnte.R
import com.oliviermarteaux.a055_rebonnte.domain.model.Medicine
import com.oliviermarteaux.a055_rebonnte.ui.screen.home.HomeViewModel
import com.oliviermarteaux.a055_rebonnte.ui.theme.Grey40
import com.oliviermarteaux.a055_rebonnte.ui.theme.Red40
import com.oliviermarteaux.localshared.composables.IntPickerDialog
import com.oliviermarteaux.localshared.composables.ItemPickerDialog
import com.oliviermarteaux.localshared.composables.SharedFilledIntTextField
import com.oliviermarteaux.shared.composables.CenteredCircularProgressIndicator
import com.oliviermarteaux.shared.composables.SharedButton
import com.oliviermarteaux.shared.composables.SharedFilledTextField
import com.oliviermarteaux.shared.composables.SharedScaffold
import com.oliviermarteaux.shared.composables.SharedToast
import com.oliviermarteaux.shared.ui.UiState
import com.oliviermarteaux.shared.ui.theme.SharedPadding
import com.oliviermarteaux.shared.ui.theme.SharedSize
import com.oliviermarteaux.shared.ui.theme.ToastPadding

@Composable
fun AddOrEditMedicineScreen(
    addOrEditMedicineViewModel: AddOrEditMedicineViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
) {
    with(addOrEditMedicineViewModel) {
        val cdAddScreenTitle =
            stringResource(R.string.creation_of_a_new_event_fill_in_the_event_data_and_validate_to_create_a_new_event)
        SharedScaffold(
            title = stringResource(R.string.creation_of_an_event),
            screenContentDescription = cdAddScreenTitle,
            onBackClick = navigateBack
        ) { paddingValues ->
            Box {
                var intPickerDialog: Boolean by rememberSaveable { mutableStateOf(false) }
                fun toggleIntPickerDialog() { intPickerDialog = !intPickerDialog }

                var aislePickerDialog: Boolean by rememberSaveable { mutableStateOf(false) }
                fun toggleAislePickerDialog() { aislePickerDialog = !aislePickerDialog }

                AddScreenBody(
                    medicine = medicine,
                    modifier = Modifier.testTag("AddOrEditMedicineScreen"),
                    updateMedicineName = ::updateMedicineName,
                    addMedicine = {
                        addMedicine(medicine.stock){navigateBack()}
                    },
                    paddingValues = paddingValues,
                    toggleIntPickerDialog = ::toggleIntPickerDialog,
                    toggleAislePickerDialog = ::toggleAislePickerDialog,
                )
                when {
                    addOrEditMedicineUiState is UiState.Loading -> { CenteredCircularProgressIndicator() }

                    networkError -> SharedToast(
                        text = stringResource(R.string.network_error_check_your_internet_connection),
                        bottomPadding = ToastPadding.medium
                    )

                    unknownError -> SharedToast(
                        text = stringResource(R.string.an_unknown_error_occurred),
                        bottomPadding = ToastPadding.medium
                    )
                    intPickerDialog -> IntPickerDialog(
                        show = true,
                        value = medicine.stock,
                        range = 0..50,
                        onDismiss = { toggleIntPickerDialog() },
                        onConfirm = {
                            updateMedicineStock(it)
                            toggleIntPickerDialog()
                        }
                    )
                    aislePickerDialog -> ItemPickerDialog(
                        show = true,
                        visibleCount = 3,
                        itemList = homeViewModel.aisleList,
                        selectedItem =  homeViewModel.aisleList[0],
                        onDismiss =  {
                            toggleAislePickerDialog()
                        },
                        onConfirm = {
                            updateMedicineAisle(it)
                            toggleAislePickerDialog()
                        },
                        itemLabel = { aisle -> aisle.name }
                    )
                }
            }
        }
    }
}

@Composable
fun AddScreenBody(
    medicine: Medicine,
    modifier: Modifier = Modifier,
    updateMedicineName: (String) -> Unit,
    addMedicine: () -> Unit,
    paddingValues: PaddingValues,
    toggleIntPickerDialog: () -> Unit,
    toggleAislePickerDialog: () -> Unit,
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
            toggleIntPickerDialog = toggleIntPickerDialog,
            toggleAislePickerDialog = toggleAislePickerDialog
        )

        AddScreenSaveButton(
            onClick = addMedicine,
            medicine = medicine
        )
    }
}


@Composable
fun AddScreenTextForm(
    medicine:Medicine,
    updateMedicineName: (String) -> Unit,
    toggleIntPickerDialog: () -> Unit,
    toggleAislePickerDialog: () -> Unit,
){
    with(medicine) {
        //_ Medicine name
        SharedFilledTextField(
            value = name,
            onValueChange = { updateMedicineName(it) },
            label = stringResource(R.string.new_event),
            textFieldModifier = Modifier.fillMaxWidth(),
            isError = name.isEmpty(),
            errorText = stringResource(R.string.please_enter_a_title),
            bottomPadding = SharedPadding.large
        )

        //_ Medicine aisle
        SharedFilledTextField(
            value = aisle.name,
            onValueChange = { },
            label = stringResource(R.string.tap_here_to_enter_your_description),
            textFieldModifier = Modifier.fillMaxWidth(),
            isError = aisle.name.isEmpty(),
            errorText = stringResource(R.string.please_enter_a_description),
            bottomPadding = SharedPadding.large,
            modifier = Modifier.clickable{ toggleAislePickerDialog() },
            enabled =  false
        )

        //_ Medicine stock
        SharedFilledIntTextField(
            value = stock,
            onValueChange = {},
            onClick = { toggleIntPickerDialog() },
            label = stringResource(R.string.tap_here_to_enter_your_description),
            textFieldModifier = Modifier.fillMaxWidth(),
            isError = stock.toString().isEmpty(),
            errorText = stringResource(R.string.please_enter_a_description),
            bottomPadding = SharedPadding.large,
            readOnly = true,
            enabled = false
        )
    }
}
@Composable
fun AddScreenSaveButton(
    medicine: Medicine,
    onClick: () -> Unit
){
    SharedButton(
        text = stringResource(R.string.validate),
        onClick = onClick,
        shape = MaterialTheme.shapes.extraSmall,
        modifier = Modifier
            .fillMaxWidth()
            .height(SharedSize.medium),
        colors = ButtonDefaults.buttonColors(
            containerColor = Red40,
            disabledContainerColor = Grey40,
        ),
        textColor = White,
        enabled = (
                medicine.name.isNotEmpty()
                        && medicine.aisle.name.isNotEmpty()
                        && medicine.stock.toString().isNotEmpty()
                ),
    )
}