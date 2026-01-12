package com.oliviermarteaux.a055_rebonnte.ui.screen.home

import com.oliviermarteaux.shared.firebase.firestore.domain.model.Post

enum class SortOption(val comparator: Comparator<Post>) {
    TITLE(compareBy { it.title }),
    DATE_ASCENDING(compareBy { it.date }),
    DATE_DESCENDING(compareByDescending { it.date })
}