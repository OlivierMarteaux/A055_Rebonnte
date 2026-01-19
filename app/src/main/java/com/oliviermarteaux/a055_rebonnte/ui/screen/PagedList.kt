package com.oliviermarteaux.a055_rebonnte.ui.screen

import com.google.firebase.firestore.DocumentSnapshot

data class PagedList<T>(
    val items: List<T>,
    val lastSnapshot: DocumentSnapshot?,
    val isLastPage: Boolean
)