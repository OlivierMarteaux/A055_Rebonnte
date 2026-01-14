package com.oliviermarteaux.a055_rebonnte.ui.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.oliviermarteaux.shared.composables.spacer.SpacerMedium
import com.oliviermarteaux.shared.composables.texts.TextTitleMedium
import com.oliviermarteaux.shared.composables.texts.TextTitleSmall
import com.oliviermarteaux.shared.ui.theme.SharedPadding

@Composable
fun RebonnteItemCard(
    title: String,
    text: String = "",
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp),
        onClick = onClick
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
        ){
            SpacerMedium()
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(SharedPadding.medium),
            ) {
                TextTitleMedium(text = title)
                Spacer(Modifier.padding(SharedPadding.xxs))

                TextTitleSmall(text = text)
            }
        }
    }
}