package com.oliviermarteaux.a055_rebonnte.di

import com.oliviermarteaux.a055_rebonnte.data.repository.AisleRepository
import com.oliviermarteaux.a055_rebonnte.data.repository.MedicineRepository
import com.oliviermarteaux.shared.firebase.authentication.data.repository.UserRepository
import com.oliviermarteaux.shared.firebase.firestore.data.repository.PostRepository

interface RebonnteContainer {
    val userRepository: UserRepository
    val postRepository: PostRepository
    val aisleRepository:  AisleRepository
    val medicineRepository: MedicineRepository
}