package com.oliviermarteaux.a055_rebonnte.di

import com.oliviermarteaux.shared.firebase.authentication.data.repository.UserRepository
import com.oliviermarteaux.shared.firebase.firestore.data.repository.PostRepository

interface RebonnteContainer {
    val userRepository: UserRepository
    val postRepository: PostRepository
}