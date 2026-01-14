package com.oliviermarteaux.a055_rebonnte.ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.CollectionInfo
import androidx.compose.ui.semantics.CollectionItemInfo
import androidx.compose.ui.semantics.collectionInfo
import androidx.compose.ui.semantics.collectionItemInfo
import androidx.compose.ui.semantics.semantics
import com.oliviermarteaux.shared.ui.theme.SharedPadding

@Composable
fun <T> RebonnteItemList(
    modifier: Modifier = Modifier,
    itemList: List<T>,
    itemTitle: (T) -> String? = { null },
    getItemTitle: @Composable (T) -> String? = { null },
    itemText: @Composable (T) -> String,
    onItemClick: (T) -> Unit = {}
) {
    Column (modifier = modifier ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(SharedPadding.xs),
            modifier = Modifier.semantics{
                collectionInfo = CollectionInfo(
                    rowCount = itemList.size,
                    columnCount = 1
                )
            }
        ) {
            itemsIndexed(itemList) { index, item ->
                RebonnteItemCard(
                    title = itemTitle(item)?:getItemTitle(item)?:"",
                    text = itemText(item),
                    onClick = { onItemClick(item) },
                    modifier = Modifier.semantics {
                        collectionItemInfo = CollectionItemInfo(index, 1, 0, 1)
                    }
                )
            }
        }
    }
}