package com.oliviermarteaux.a055_rebonnte.ui.screen.addAisle

import android.content.res.Configuration
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.oliviermarteaux.a055_rebonnte.R
import com.oliviermarteaux.a055_rebonnte.domain.model.Aisle
import com.oliviermarteaux.a055_rebonnte.ui.theme.Grey40
import com.oliviermarteaux.a055_rebonnte.ui.theme.Red40
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
fun AddAisleScreen(
    addAisleViewModel: AddAisleViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
) {
    with(addAisleViewModel) {
        val cdAddScreenTitle =
            stringResource(R.string.creation_of_a_new_event_fill_in_the_event_data_and_validate_to_create_a_new_event)
        SharedScaffold(
            title = stringResource(R.string.creation_of_an_event),
            screenContentDescription = cdAddScreenTitle,
            onBackClick = navigateBack
        ) { paddingValues ->
            Box {
                AddAisleScreenBody(
                    aisle = aisle,
                    modifier = Modifier.testTag("Add Screen"),
                    updateAisleName = ::updateAisleName,
                    addAisle = { addAisle(onResult = navigateBack) },
                    paddingValues = paddingValues,
                )
                if (addAisleUiState is UiState.Loading) { CenteredCircularProgressIndicator() }
                if (networkError) SharedToast(
                    text = stringResource(R.string.network_error_check_your_internet_connection),
                    bottomPadding = ToastPadding.medium
                )
                if (unknownError) SharedToast(
                    text = stringResource(R.string.an_unknown_error_occurred),
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
        AddAisleScreenSaveButton(
            onClick = addAisle,
            aisle = aisle
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
            label = stringResource(R.string.new_event),
            textFieldModifier = Modifier.fillMaxWidth(),
            isError = name.isEmpty(),
            errorText = stringResource(R.string.please_enter_a_title),
            bottomPadding = SharedPadding.large
        )
    }
}

@Composable
fun AddAisleScreenSaveButton(
    aisle: Aisle,
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
        enabled = (aisle.name.isNotEmpty()),
    )
}