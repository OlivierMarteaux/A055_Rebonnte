package com.oliviermarteaux.localshared.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.oliviermarteaux.shared.ui.theme.SharedPadding
import com.oliviermarteaux.shared.composables.SharedAlertDialog
import com.oliviermarteaux.shared.composables.SharedButton
import com.oliviermarteaux.shared.composables.SharedOutlinedEmail
import com.oliviermarteaux.shared.composables.SharedScaffold
import com.oliviermarteaux.shared.composables.SharedToast
import com.oliviermarteaux.shared.extensions.isValidEmail
import com.oliviermarteaux.shared.compose.R
import com.oliviermarteaux.shared.firebase.authentication.ui.screen.reset.ResetViewModel

/**
 * A screen for resetting the user's password.
 *
 * @param modifier The modifier to apply to this screen.
 * @param navigateToLoginScreen A function to call to navigate to the login screen.
 * @param onBackClick A function to call when the back button is clicked.
 * @param resetViewModel The view model for this screen.
 */
@Composable
fun ResetScreen(
    modifier: Modifier = Modifier,
    navigateToLoginScreen: () -> Unit,
    onBackClick: () -> Unit = {},
    resetViewModel: ResetViewModel = hiltViewModel(),
    logoDrawableRes: Int,
    landscapeHorizontalPadding: Dp = 85.dp,
    landscapeCentralPadding: Dp = 85.dp,
    formPortraitHorizontalPadding: Dp = 24.dp,
    imageModifier: Modifier = Modifier.fillMaxWidth()
) {

    SharedScaffold(
        modifier = modifier,
        title = stringResource(R.string.recover_password),
        onBackClick = onBackClick
    ) { contentPadding ->
        with (resetViewModel) {
            Box {
                ResetBody(
                    email = email,
                    onEmailChange = ::onEmailChange,
                    sendPasswordResetEmail = ::sendPasswordResetEmail,
                    alertDialog = alertDialog,
                    navigateToLoginScreen = navigateToLoginScreen,
                    contentPadding = contentPadding,
                    logoDrawableRes = logoDrawableRes,
                    modifier = modifier,
                    imageModifier = imageModifier,
                    landscapeHorizontalPadding = landscapeHorizontalPadding,
                    landscapeCentralPadding = landscapeCentralPadding,
                    formPortraitHorizontalPadding = formPortraitHorizontalPadding
                )
                if(unknownError) SharedToast(text = stringResource(R.string.an_unknown_error_occurred))
                if(networkError) SharedToast(
                    text = stringResource(R.string.network_error_check_your_internet_connection),
                    bottomPadding = 120
                )
            }
        }
    }
}

/**
 * A composable for the body of the reset screen.
 *
 * @param email The user's email address.
 * @param onEmailChange A function to call when the email changes.
 * @param sendPasswordResetEmail A function to call to send a password reset email.
 * @param alertDialog A boolean indicating if the alert dialog should be shown.
 * @param navigateToLoginScreen A function to call to navigate to the login screen.
 */
@Composable
private fun ResetBody(
    email: String,
    onEmailChange: (String) -> Unit,
    sendPasswordResetEmail: (String) -> Unit,
    alertDialog: Boolean,
    navigateToLoginScreen: () -> Unit,
    contentPadding: PaddingValues,
    logoDrawableRes: Int,
    modifier: Modifier,
    landscapeHorizontalPadding: Dp,
    landscapeCentralPadding: Dp,
    formPortraitHorizontalPadding: Dp,
    imageModifier: Modifier
) {
    ImageScaffold(
        image = painterResource(id = logoDrawableRes),
        imageModifier = imageModifier,
        landscapeHorizontalPadding = landscapeHorizontalPadding,
        landscapeCentralPadding = landscapeCentralPadding,
        formPortraitHorizontalPadding = formPortraitHorizontalPadding,
        innerPadding = contentPadding,
        modifier =  modifier
    ){
        Spacer(Modifier.height(24.dp))
        Text(
            text = stringResource(R.string.receive_instructions_to_this_email_that_explain_how_to_reset_your_password),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(SharedPadding.medium))

        SharedOutlinedEmail(
            value = email,
            onValueChange = { onEmailChange(it) },
            label = stringResource(R.string.email),
            imeAction = ImeAction.Done,
            textFieldModifier = Modifier.fillMaxWidth(),
            errorText = when {
                email.isEmpty() -> stringResource(R.string.enter_your_email_address_to_continue)
                !email.isValidEmail() -> stringResource(R.string.incorrect_email_address)
                else -> null
            }
        )
        Spacer(Modifier.height(SharedPadding.medium))

        SharedButton(
            text = stringResource(R.string.send),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 60.dp)
        ) { sendPasswordResetEmail(email) }
        Spacer(Modifier.height(SharedPadding.medium))
    }
    AnimatedVisibility(alertDialog) {
        SharedAlertDialog(
            title = stringResource(R.string.check_your_email),
            text = stringResource(R.string.receive_instructions_to_this_email_that_explain_how_to_reset_your_password, email),
            onConfirm = navigateToLoginScreen,
            confirmText = stringResource(R.string.ok)
        )
    }
}

