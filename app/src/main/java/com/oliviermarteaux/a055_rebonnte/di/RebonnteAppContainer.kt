package com.oliviermarteaux.a055_rebonnte.di

import android.content.Context
import com.oliviermarteaux.shared.firebase.authentication.data.repository.UserFirebaseRepository
import com.oliviermarteaux.shared.firebase.authentication.data.repository.UserRepository
import com.oliviermarteaux.shared.firebase.authentication.data.service.UserApi
import com.oliviermarteaux.shared.firebase.authentication.data.service.UserFirebaseApi
import com.oliviermarteaux.shared.firebase.firestore.data.repository.PostFirebaseRepository
import com.oliviermarteaux.shared.firebase.firestore.data.repository.PostRepository
import com.oliviermarteaux.shared.firebase.firestore.data.service.PostApi
import com.oliviermarteaux.shared.firebase.firestore.data.service.PostFirebaseApi

class RebonnteAppContainer(context: Context)
    : RebonnteContainer {
    private val userApi: UserApi = UserFirebaseApi(context)
    private val postApi: PostApi = PostFirebaseApi()

    override val userRepository: UserRepository by lazy {
        UserFirebaseRepository(userApi)
    }

    override val postRepository: PostRepository by lazy {
        PostFirebaseRepository(postApi)
    }
}