package com.oliviermarteaux.localshared.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun ClickableReadOnlyField(
    onClick: () -> Unit,
    readOnlyField: @Composable () -> Unit
) {
    Box {
        // The read-only UI (e.g. TextField with readOnly = true)
        readOnlyField()

        // Transparent clickable overlay
        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable(
                    indication = null, // no ripple
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    onClick()
                }
        )
    }
}