package com.oliviermarteaux.a055_rebonnte.di

import android.app.Application
import com.oliviermarteaux.a055_rebonnte.RebonnteApplication
import com.oliviermarteaux.a055_rebonnte.data.repository.MedicineRepository
import com.oliviermarteaux.shared.firebase.firestore.data.repository.PostRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MedicineModule {

    @Provides
    @Singleton
    fun provideMedicineRepository(
        application: Application
    ): MedicineRepository {
        val app = application as RebonnteApplication
        return app.rebonnteContainer.medicineRepository
    }
}