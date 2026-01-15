package com.oliviermarteaux.a055_rebonnte.ui.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.stringResource
import com.oliviermarteaux.a055_rebonnte.ui.theme.Grey40
import com.oliviermarteaux.a055_rebonnte.ui.theme.Red40
import com.oliviermarteaux.shared.composables.SharedButton
import com.oliviermarteaux.shared.ui.theme.SharedSize
import com.oliviermarteaux.shared.compose.R as oR

@Composable
fun RebonnteSaveButton(
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
){
    SharedButton(
        text = stringResource(oR.string.validate),
        onClick = onClick,
        shape = MaterialTheme.shapes.extraSmall,
        modifier = modifier
            .fillMaxWidth()
            .height(SharedSize.medium),
        colors = ButtonDefaults.buttonColors(
            containerColor = Red40,
            disabledContainerColor = Grey40,
        ),
        textColor = White,
        enabled = enabled,
    )
}