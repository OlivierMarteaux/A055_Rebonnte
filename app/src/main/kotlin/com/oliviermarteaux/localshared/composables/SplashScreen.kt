package com.oliviermarteaux.localshared.composables

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.oliviermarteaux.shared.composables.IconSource
import com.oliviermarteaux.shared.composables.SharedButton
import com.oliviermarteaux.shared.composables.SharedScaffold
import com.oliviermarteaux.shared.compose.R
import com.oliviermarteaux.shared.firebase.authentication.ui.screen.splash.SplashViewModel

/**
 * A screen that is displayed when the application is launched.
 *
 * @param modifier The modifier to apply to this screen.
 * @param navigateToLoginScreen A function to call to navigate to the login screen.
 */
@Composable
fun SplashScreen(
    logoDrawableRes: Int,
    modifier: Modifier = Modifier,
    splashViewModel: SplashViewModel = hiltViewModel(),
    navigateToLoginScreen: () -> Unit,
    navigateToHomeScreen: () -> Unit,
    @StringRes serverClientIdStringRes: Int,
    landscapeHorizontalPadding: Dp = 85.dp,
    landscapeCentralPadding: Dp = 85.dp,
    formPortraitHorizontalPadding: Dp = 85.dp,
    imageModifier: Modifier = Modifier,
) {
    with(splashViewModel) {
        SharedScaffold(
            modifier = modifier,
        ) { innerPadding ->
            ImageScaffold(
                image = painterResource(id = logoDrawableRes),
                modifier = Modifier.consumeWindowInsets(innerPadding),
                innerPadding = innerPadding,
                landscapeHorizontalPadding = landscapeHorizontalPadding,
                landscapeCentralPadding = landscapeCentralPadding,
                formPortraitHorizontalPadding = formPortraitHorizontalPadding,
                imageModifier = imageModifier,
            ) {
                Spacer(Modifier.height(24.dp))
                SharedButton(
                    onClick = { signInWithGoogle(serverClientIdStringRes,navigateToHomeScreen) },
                    text = stringResource(R.string.sign_in_with_Google),
                    textColor = Color.Black,
                    icon = IconSource.PainterIcon(painterResource(id = R.drawable.ic_google_logo)),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = MaterialTheme.shapes.extraSmall,
                    contentPadding = PaddingValues(16.dp),
                    modifier = Modifier.fillMaxWidth(),
                    tint = Color.Unspecified
                )
                Spacer(Modifier.height(24.dp))

                SharedButton(
                    onClick = navigateToLoginScreen,
                    text = stringResource(R.string.sign_in_with_email),
                    textColor = Color.White,
                    icon = IconSource.PainterIcon(painterResource(id = R.drawable.ic_mail_white_no_outline)),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                    shape = MaterialTheme.shapes.extraSmall,
                    contentPadding = PaddingValues(16.dp),
                    modifier = Modifier.fillMaxWidth(),
                    tint = Color.Unspecified
                )
            }
        }
    }
}