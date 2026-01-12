package com.oliviermarteaux.a055_rebonnte.ui.screen.add

import android.content.res.Configuration
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.oliviermarteaux.a055_rebonnte.R
import com.oliviermarteaux.a055_rebonnte.ui.theme.Grey40
import com.oliviermarteaux.a055_rebonnte.ui.theme.Red40
import com.oliviermarteaux.shared.firebase.firestore.domain.model.Post
import com.oliviermarteaux.shared.ui.theme.SharedPadding
import com.oliviermarteaux.shared.ui.theme.SharedSize
import com.oliviermarteaux.shared.ui.theme.ToastPadding
import com.oliviermarteaux.shared.composables.CenteredCircularProgressIndicator
import com.oliviermarteaux.shared.composables.IconSource
import com.oliviermarteaux.shared.composables.SharedButton
import com.oliviermarteaux.shared.composables.SharedCardAsyncImage
import com.oliviermarteaux.shared.composables.SharedDateTextField
import com.oliviermarteaux.shared.composables.SharedFilledTextField
import com.oliviermarteaux.shared.composables.SharedIconButton
import com.oliviermarteaux.shared.composables.SharedScaffold
import com.oliviermarteaux.shared.composables.SharedTimeTextField
import com.oliviermarteaux.shared.composables.SharedToast
import com.oliviermarteaux.shared.composables.sharedImagePicker
import com.oliviermarteaux.shared.composables.spacer.SpacerXl
import com.oliviermarteaux.shared.ui.UiState

@Composable
fun AddScreen(
    addViewModel: AddViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    navigateToCamera: ((String) -> Unit) -> Unit
) {
    with(addViewModel) {
        val cdAddScreenTitle =
            stringResource(R.string.creation_of_a_new_event_fill_in_the_event_data_and_validate_to_create_a_new_event)
        SharedScaffold(
            title = stringResource(R.string.creation_of_an_event),
            screenContentDescription = cdAddScreenTitle,
            onBackClick = navigateBack
        ) { paddingValues ->
            Box {
                AddScreenBody(
                    post = post,
                    modifier = Modifier.testTag("Add Screen"),
                    updatePostTitle = ::updatePostTitle,
                    updatePostDescription = ::updatePostDescription,
                    updatePostDate = ::updatePostDate,
                    updatePostTime = ::updatePostTime,
                    updatePostAddress = ::updatePostAddress,
                    updatePostPhoto = ::updatePostPhoto,
                    navigateToCamera = navigateToCamera,
                    addPost = { addPost(onResult = navigateBack) },
                    paddingValues = paddingValues,
                )
                if (addPostUiState is UiState.Loading) { CenteredCircularProgressIndicator() }
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
fun AddScreenBody(
    post: Post,
    modifier: Modifier = Modifier,
    updatePostTitle: (String) -> Unit,
    updatePostDescription: (String) -> Unit,
    updatePostDate: (String) -> Unit,
    updatePostTime: (String) -> Unit,
    updatePostAddress: (String) -> Unit,
    updatePostPhoto: (String) -> Unit,
    navigateToCamera: ((String) -> Unit) -> Unit,
    addPost: () -> Unit,
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
        AddScreenTextForm(
            post = post,
            updatePostTitle = updatePostTitle,
            updatePostDescription = updatePostDescription,
            updatePostDate = updatePostDate,
            updatePostTime = updatePostTime,
            updatePostAddress = updatePostAddress,
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize()
        ){
            AddScreenPhotoPickButtonsCard(
                updatePostPhoto = updatePostPhoto,
                navigateToCamera = navigateToCamera
            )
            SpacerXl()

            post.photoUrl?.let { AddScreenImagePreview(it) }
            SpacerXl()

            AddScreenSaveButton(
                onClick = addPost,
                post = post
            )
        }
    }
}

@Composable
fun AddScreenTextForm(
    post:Post,
    updatePostTitle: (String) -> Unit,
    updatePostDescription: (String) -> Unit,
    updatePostDate: (String) -> Unit,
    updatePostTime: (String) -> Unit,
    updatePostAddress: (String) -> Unit,
){
    with(post) {
        //_ Event title
        SharedFilledTextField(
            value = title,
            onValueChange = { updatePostTitle(it) },
            label = stringResource(R.string.new_event),
            textFieldModifier = Modifier.fillMaxWidth(),
            isError = title.isEmpty(),
            errorText = stringResource(R.string.please_enter_a_title),
            bottomPadding = SharedPadding.large
        )

        //_ Event description
        SharedFilledTextField(
            value = description,
            onValueChange = { updatePostDescription(it) },
            label = stringResource(R.string.tap_here_to_enter_your_description),
            textFieldModifier = Modifier.fillMaxWidth(),
            isError = description.isEmpty(),
            errorText = stringResource(R.string.please_enter_a_description),
            bottomPadding = SharedPadding.large
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(SharedPadding.medium)
        ) {
            //_ date
            SharedDateTextField(
                date = localeDateString,
                onDateChange = { updatePostDate(it) },
                modifier = Modifier.weight(weight = 1f, fill = true),
                textFieldModifier = Modifier.testTag("Date")
            )
            //_ time
            SharedTimeTextField(
                time = localeTimeString,
                onTimeChange = { updatePostTime(it) },
                modifier = Modifier.weight(weight = 1f, fill = true),
                textFieldModifier = Modifier.testTag("Time")
            )
        }

        //_ address
        SharedFilledTextField(
            value = address.street,
            onValueChange = { updatePostAddress(it) },
            label = stringResource(R.string.address),
            placeholder = stringResource(R.string.enter_full_address),
            textFieldModifier = Modifier.fillMaxWidth(),
            isError = address.street.isEmpty(),
            errorText = stringResource(R.string.please_enter_an_address),
            bottomPadding = SharedPadding.xxl,
            imeAction = ImeAction.Done
        )
    }
}

@Composable
fun AddScreenPhotoPickButtonsCard(
    updatePostPhoto: (String) -> Unit,
    navigateToCamera: ((String) -> Unit) -> Unit
){
    Row(
        horizontalArrangement = Arrangement.spacedBy(SharedPadding.medium)
    ) {
        CameraPhotoPickButton { navigateToCamera { photoShot -> updatePostPhoto(photoShot) } }

        LocalePhotoPickButton { selectedPhoto -> updatePostPhoto(selectedPhoto) }
    }
}

@Composable
fun LocalePhotoPickButton(onClick: (String) -> Unit) {
    val cdLocalePhotoButton =
        stringResource(R.string.locale_photo_button_click_here_to_pick_a_photo_from_your_device)
    // Get the ImagePicker launcher
    val imagePickerLauncher = sharedImagePicker { onClick(it.toString()) }
    SharedIconButton(
        icon = IconSource.VectorIcon(Icons.Default.AttachFile),
        shape = MaterialTheme.shapes.large,
        tint = White,
        colors = IconButtonDefaults.iconButtonColors(containerColor = Color.Red),
        modifier = Modifier
            .testTag("Locale Photo Button")
            .size(SharedSize.medium)
            .semantics {
                contentDescription = cdLocalePhotoButton
            }
    ) {
        imagePickerLauncher.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
    }
}

@Composable
fun CameraPhotoPickButton(onClick: () -> Unit) {
    val cdCameraButton = stringResource(R.string.camera_button_click_here_to_take_a_photo)
    SharedIconButton(
        icon = IconSource.VectorIcon(Icons.Outlined.CameraAlt),
        shape = MaterialTheme.shapes.large,
        tint = Black,
        colors = IconButtonDefaults.iconButtonColors(containerColor = White),
        onClick = onClick,
        modifier = Modifier
            .size(SharedSize.medium)
            .semantics {
                contentDescription = cdCameraButton
            }
    )
}

@Composable
fun AddScreenImagePreview(photoUrl: String){
    SharedCardAsyncImage(
        photoUri = photoUrl,
        imageModifier = Modifier.size(SharedSize.xxl)
    )
}

@Composable
fun AddScreenSaveButton(
    post: Post,
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
                post.title.isNotEmpty()
                        && post.description.isNotEmpty()
                        && post.localeDateString.isNotEmpty()
                        && post.localeTimeString.isNotEmpty()
                        && post.address.street.isNotEmpty()
                        && !post.photoUrl.isNullOrEmpty()
                ),
    )
}