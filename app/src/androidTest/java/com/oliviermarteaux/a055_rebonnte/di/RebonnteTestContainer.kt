package com.oliviermarteaux.a055_rebonnte.di

import android.content.Context
import com.oliviermarteaux.a055_rebonnte.fake.PostFakeRepository
import com.oliviermarteaux.a055_rebonnte.data.repository.AisleRepository
import com.oliviermarteaux.a055_rebonnte.data.repository.MedicineRepository
import com.oliviermarteaux.a055_rebonnte.fake.AisleFakeRepository
import com.oliviermarteaux.a055_rebonnte.fake.MedicineFakeRepository
import com.oliviermarteaux.a055_rebonnte.fake.UserFakeRepository
import com.oliviermarteaux.shared.firebase.authentication.data.repository.UserRepository
import com.oliviermarteaux.shared.firebase.firestore.data.repository.PostRepository

class RebonnteTestContainer(context: Context) : RebonnteContainer {

    override val userRepository: UserRepository =
        UserFakeRepository()

    override val postRepository: PostRepository =
        PostFakeRepository()

    override val aisleRepository: AisleRepository =
        AisleFakeRepository()

    override val medicineRepository: MedicineRepository =
        MedicineFakeRepository()
}