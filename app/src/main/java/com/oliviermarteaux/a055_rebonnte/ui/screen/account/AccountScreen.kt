package com.oliviermarteaux.a055_rebonnte.ui.screen.account

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.hideFromAccessibility
import androidx.compose.ui.semantics.semantics
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.oliviermarteaux.a055_rebonnte.R
import com.oliviermarteaux.a055_rebonnte.ui.theme.Red40
import com.oliviermarteaux.a055_rebonnte.ui.theme.White
import com.oliviermarteaux.shared.composables.CenteredCircularProgressIndicator
import com.oliviermarteaux.shared.composables.SharedBottomAppBar
import com.oliviermarteaux.shared.composables.SharedFilledTextField
import com.oliviermarteaux.shared.composables.SharedScaffold
import com.oliviermarteaux.shared.composables.SharedToast
import com.oliviermarteaux.shared.composables.extensions.cdButtonSemantics
import com.oliviermarteaux.shared.composables.spacer.SpacerLarge
import com.oliviermarteaux.shared.composables.spacer.SpacerSmall
import com.oliviermarteaux.shared.composables.spacer.SpacerXl
import com.oliviermarteaux.shared.firebase.authentication.domain.model.User
import com.oliviermarteaux.shared.ui.UiState
import com.oliviermarteaux.shared.ui.theme.SharedPadding
import com.oliviermarteaux.shared.ui.theme.ToastPadding

@Composable
fun AccountScreen(
    accountViewModel: AccountViewModel = hiltViewModel(),
    navController: NavController
) {
    val cdProfileScreen =
        stringResource(R.string.profile_screen_here_are_displayed_your_data_and_notifications_settings)
    with(accountViewModel) {
        SharedScaffold(
            title = stringResource(R.string.user_profile),
            screenContentDescription = cdProfileScreen ,
            avatarUrl = user.photoUrl,
            topAppBarModifier = Modifier.padding(horizontal = SharedPadding.small),
            bottomBar = { SharedBottomAppBar(navController = navController) }
        ) { paddingValues ->
            Box {
                AccountScreenBody(
                    user = user,
                    notificationState = notificationState,
                    toggleNotifications = ::toggleNotifications,
                    modifier = Modifier
                        .testTag("Profile Screen")
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = SharedPadding.large)
                )

                when {
                    userUiState is UiState.Loading -> { CenteredCircularProgressIndicator() }
                    userUiState is UiState.Error -> {
                        SharedToast(
                            text = stringResource(R.string.an_unknown_error_occurred),
                            bottomPadding = ToastPadding.high
                        )
                    }
                    networkError -> {
                        SharedToast(
                            text = stringResource(R.string.network_error_check_your_internet_connection),
                            bottomPadding = ToastPadding.medium
                        )
                    }
                    authError -> {
                        SharedToast(
                            text = stringResource(R.string.user_is_disconnected),
                            bottomPadding = ToastPadding.veryHigh
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AccountScreenBody(
    user: User,
    modifier: Modifier = Modifier,
    notificationState: Boolean = true,
    toggleNotifications: () -> Unit
){
    Column(
        modifier = modifier
    ) {
        val cdStateDescription = stringResource(R.string.not_editable)
        SharedFilledTextField(
            value = user.getComputedFullName(),
            label = stringResource(R.string.name),
            textFieldModifier = Modifier
                .semantics { this.contentDescription = cdStateDescription }
                .fillMaxWidth(),
            readOnly = true
        )
        SpacerLarge()

        SharedFilledTextField(
            value = user.email,
            label = stringResource(R.string.email),
            textFieldModifier = Modifier
                .semantics { this.contentDescription = cdStateDescription }
                .fillMaxWidth(),
            readOnly = true
        )
        SpacerXl()

        val cdNotifications =
            if (notificationState) stringResource(R.string.notification_are_enabled_double_tap_to_disable_it)
            else stringResource(R.string.notification_are_disabled_double_tap_to_enable_it)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Switch(
                checked = notificationState,
                onCheckedChange = { toggleNotifications() },
                modifier = Modifier.cdButtonSemantics(cdNotifications),
                colors = SwitchDefaults.colors(
                    checkedThumbColor = White,
                    checkedTrackColor = Red40,
                )
            )
            SpacerSmall()
            Text(
                text = stringResource(R.string.notifications),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.semantics { hideFromAccessibility() }
            )
        }
    }
}