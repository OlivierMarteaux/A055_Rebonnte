package com.oliviermarteaux.a055_rebonnte.ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.CollectionInfo
import androidx.compose.ui.semantics.CollectionItemInfo
import androidx.compose.ui.semantics.collectionInfo
import androidx.compose.ui.semantics.collectionItemInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.oliviermarteaux.shared.ui.theme.SharedPadding

@Composable
fun <T> RebonnteItemList(
    modifier: Modifier = Modifier,
    itemList: List<T>,
    itemId: (T) -> String,
    itemTitle: (T) -> String? = { null },
    getItemTitle: @Composable (T) -> String? = { null },
    itemText: @Composable (T) -> String,
    onItemClick: (T) -> Unit = {},
    isLastPage: Boolean,
    loadNextPage: () -> Unit,
    itemModifier: Modifier = Modifier,
    lazyListModifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()

    Column (modifier = modifier ) {
        LazyColumn(
            state = listState,
            verticalArrangement = Arrangement.spacedBy(SharedPadding.xs),
            modifier = lazyListModifier.semantics{
                collectionInfo = CollectionInfo(
                    rowCount = itemList.size,
                    columnCount = 1
                )
            }
        ) {
            itemsIndexed(
                items = itemList,
                key = { index, item -> itemId(item) } // 🔑 MUST be stable & unique
            ) { index, item ->
                RebonnteItemCard(
                    title = itemTitle(item)?:getItemTitle(item)?:"",
                    text = itemText(item),
                    onClick = { onItemClick(item) },
                    modifier = itemModifier.semantics {
                        collectionItemInfo = CollectionItemInfo(index, 1, 0, 1)
                    }
                )
            }
            item {
                if (!isLastPage) {
                    LaunchedEffect(Unit) {
                        loadNextPage()
                    }
                    Column (
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .height(80.dp)
                            .fillMaxWidth()
                    ) { CircularProgressIndicator() }
                }
            }
        }
    }
}