package com.oliviermarteaux.localshared.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription as cdSemantics
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.onClick

@Composable
fun ClickableReadOnlyField(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String = "",
    isError: Boolean = false,
    errorText: String = "",
    readOnlyField: @Composable () -> Unit,
) {
    Box (
        modifier = modifier.clearAndSetSemantics{
            cdSemantics = contentDescription
            onClick(
                label = "Activate"
            ) {
                onClick()
                true
            }
            if (isError) { error(errorText) }
        }
    ){
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