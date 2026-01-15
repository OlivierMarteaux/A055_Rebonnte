package com.oliviermarteaux.a055_rebonnte.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.oliviermarteaux.shared.compose.R as oR
import com.oliviermarteaux.a055_rebonnte.ui.theme.Grey40
import com.oliviermarteaux.a055_rebonnte.ui.theme.Red40
import com.oliviermarteaux.shared.composables.IconSource
import com.oliviermarteaux.shared.composables.SharedButton
import com.oliviermarteaux.shared.composables.SharedIcon
import com.oliviermarteaux.shared.composables.spacer.SpacerLarge
import com.oliviermarteaux.shared.composables.texts.TextTitleMedium
import com.oliviermarteaux.shared.composables.texts.TextTitleSmall

@Composable
fun RebonnteErrorScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
    loadItems: () -> Unit
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .consumeWindowInsets(contentPadding)   // 👈 prevents double padding,
            .fillMaxSize()
            .padding(contentPadding)
            .padding(horizontal = 126.dp),
    ){
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(64.dp)
                .background(color = Grey40, shape = CircleShape)
        ) {
            SharedIcon(
                icon = IconSource.VectorIcon(Icons.Filled.PriorityHigh),
                modifier = Modifier.size(32.dp),
                tint = White,
            )
        }
        SpacerLarge()
        TextTitleMedium(text = stringResource(oR.string.error))
        TextTitleSmall(
            text = stringResource(oR.string.an_error_as_occurred_please_try_again_later),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(35.dp))
        SharedButton(
            text = stringResource(oR.string.try_again),
            onClick = loadItems,
            shape = MaterialTheme.shapes.extraSmall,
            colors = ButtonDefaults.buttonColors(containerColor = Red40),
            textColor = White
        )
    }
}