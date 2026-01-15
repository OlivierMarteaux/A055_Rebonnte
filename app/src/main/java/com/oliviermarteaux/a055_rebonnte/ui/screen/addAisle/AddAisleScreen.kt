package com.oliviermarteaux.a055_rebonnte.ui.screen.addAisle

import android.content.res.Configuration
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
import com.oliviermarteaux.shared.compose.R as oR
import com.oliviermarteaux.a055_rebonnte.domain.model.Aisle
import com.oliviermarteaux.a055_rebonnte.ui.composable.RebonnteSaveButton
import com.oliviermarteaux.a055_rebonnte.ui.navigation.RebonnteScreen
import com.oliviermarteaux.shared.composables.CenteredCircularProgressIndicator
import com.oliviermarteaux.shared.composables.SharedFilledTextField
import com.oliviermarteaux.shared.composables.SharedScaffold
import com.oliviermarteaux.shared.composables.SharedToast
import com.oliviermarteaux.shared.ui.UiState
import com.oliviermarteaux.shared.ui.theme.SharedPadding
import com.oliviermarteaux.shared.ui.theme.ToastPadding

@Composable
fun AddAisleScreen(
    addAisleViewModel: AddAisleViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
) {
    with(addAisleViewModel) {
        val cdItem = stringResource(R.string.aisle)
        val cdScreen = stringResource(
            R.string.creation_of_a_new_fill_in_the_fields_and_validate_to_create_a_new,
            cdItem,
            cdItem
        )
        SharedScaffold(
            title = stringResource(RebonnteScreen.AddAisle.titleRes),
            screenContentDescription = cdScreen,
            onBackClick = navigateBack
        ) { paddingValues ->
            Box {
                AddAisleScreenBody(
                    aisle = aisle,
                    modifier = Modifier.testTag("AddAisleScreen"),
                    updateAisleName = ::updateAisleName,
                    addAisle = { addAisle(onResult = navigateBack) },
                    paddingValues = paddingValues,
                )
                if (addAisleUiState is UiState.Loading) { CenteredCircularProgressIndicator() }
                if (networkError) SharedToast(
                    text = stringResource(oR.string.network_error_check_your_internet_connection),
                    bottomPadding = ToastPadding.medium
                )
                if (unknownError) SharedToast(
                    text = stringResource(oR.string.an_unknown_error_occurred),
                    bottomPadding = ToastPadding.medium
                )
            }
        }
    }
}

@Composable
fun AddAisleScreenBody(
    aisle: Aisle,
    modifier: Modifier = Modifier,
    updateAisleName: (String) -> Unit,
    addAisle: () -> Unit,
    paddingValues: PaddingValues,
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
        AddAisleScreenTextForm(
            aisle = aisle,
            updateAisleName = updateAisleName,
        )
        RebonnteSaveButton(
            onClick = addAisle,
            enabled = (aisle.name.isNotEmpty())
        )
    }
}

@Composable
fun AddAisleScreenTextForm(
    aisle:Aisle,
    updateAisleName: (String) -> Unit,
){
    with(aisle) {
        //_ Aisle name
        SharedFilledTextField(
            value = name,
            onValueChange = { updateAisleName(it) },
            label = stringResource(oR.string.name),
            textFieldModifier = Modifier.fillMaxWidth(),
            isError = name.isEmpty(),
            errorText = stringResource(R.string.please_enter_a_name),
            bottomPadding = SharedPadding.large
        )
    }
}