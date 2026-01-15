package com.oliviermarteaux.localshared.composables

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.oliviermarteaux.shared.composables.SharedImage

/**
 * A scaffold that displays an icon and some content.
 *
 * @param modifier The modifier to apply to this scaffold.
 * @param content The content to display.
 */
@Composable
fun ImageScaffold(
    image: Painter,
    modifier: Modifier = Modifier,
    imageModifier: Modifier = Modifier,
    landscapeHorizontalPadding: Dp = 0.dp,
    landscapeCentralPadding: Dp = 0.dp,
    formPortraitHorizontalPadding: Dp = 0.dp,
    innerPadding: PaddingValues = PaddingValues(),
    content: @Composable () -> Unit
) {
    val configuration = LocalConfiguration.current
    val orientation = configuration.orientation

    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.padding(innerPadding)
        ) {
            SharedImage(
                painter = image,
                modifier = imageModifier.weight(1f)
            )
            Column(
                modifier = Modifier
                    .weight(2f)
                    .padding(horizontal = formPortraitHorizontalPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                content()
            }
        }
    } else {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxHeight()
                .padding(innerPadding)
                .padding(horizontal = landscapeHorizontalPadding)
                .padding(bottom = 24.dp),
        ) {
            SharedImage(
                painter = image,
                modifier = imageModifier.weight(1f)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(2f)
                    .padding(start = landscapeCentralPadding, bottom = 24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                content()
            }
        }
    }
}