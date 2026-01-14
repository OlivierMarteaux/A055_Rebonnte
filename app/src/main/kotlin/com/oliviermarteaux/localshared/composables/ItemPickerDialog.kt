package com.oliviermarteaux.localshared.composables

import android.R.attr.text
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.collections.getOrNull
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.times
import com.oliviermarteaux.a055_rebonnte.domain.model.Aisle

@Composable
fun <T> ItemPickerDialog(
    show: Boolean,
    itemList: List<T>,
    selectedItem: T,
    onDismiss: () -> Unit,
    onConfirm: (T) -> Unit,
    visibleCount: Int = 5,
    title: String = "Select item",
    itemLabel: (T) -> String // 🔑 lambda to get display name
) {
    if (!show) return

    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = itemList.indexOf(selectedItem).coerceAtLeast(0)
    )

    var currentItem by remember { mutableStateOf(selectedItem) }

    LaunchedEffect(listState.isScrollInProgress) {
        if (!listState.isScrollInProgress) {
            val centerIndex = listState.firstVisibleItemIndex
            itemList.getOrNull(centerIndex)?.let { currentItem = it }
        }
    }
    val centerOffset = visibleCount / 2

    val spacedItemList: List<T?> = remember(itemList, visibleCount) {
        List(2) { null } +
                itemList +
                List(2) { null }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            LazyColumn(
                state = listState,
                modifier = Modifier.height(visibleCount * 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(spacedItemList) { item ->
                    val label: String = item?.let { itemLabel(it) }?:""
                    Text(
                        text = label,
                        style = if (item == currentItem)
                            MaterialTheme.typography.titleMedium
                        else
                            MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(currentItem) }) { Text("OK") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}