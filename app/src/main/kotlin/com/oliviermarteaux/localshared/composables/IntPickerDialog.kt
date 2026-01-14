package com.oliviermarteaux.localshared.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun IntPickerDialog(
    show: Boolean,
    value: Int,
    range: IntRange,
    visibleCount: Int = 5,
    title: String = "Select value",
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    if (!show) return

    val values = remember(range) { List(4) { "min" } + range.toList() + List(4) { "max"} }
    val initialIndex = values.indexOf(value) - 3 .coerceAtLeast(0)

    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = initialIndex
    )

    var selectedValue by rememberSaveable { mutableIntStateOf(value) }

    LaunchedEffect(listState.isScrollInProgress) {
        if (!listState.isScrollInProgress) {
            val centerIndex = listState.firstVisibleItemIndex + ( visibleCount ) / 2
            values.getOrNull(centerIndex)?.let { item ->
                selectedValue = when (item) {
                    is Int -> item
                    "min" -> range.first
                    "max" -> range.last
                    else -> 0
                }
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.height((visibleCount * 30).dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(values) { item ->
                        Text(
                            text = when(item) {
                                is Int -> item.toString()
                                else -> ""
                            },
                            fontSize = when (item){
                                selectedValue -> 40.sp
//                                selectedValue - 1 -> 25.sp
//                                selectedValue + 1 -> 25.sp
//                                selectedValue - 2 -> 20.sp
//                                selectedValue + 2 -> 20.sp
                                else -> 15.sp
                            },
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(selectedValue) }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
