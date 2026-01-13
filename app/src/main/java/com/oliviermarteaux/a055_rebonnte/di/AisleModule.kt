package com.oliviermarteaux.a055_rebonnte.di

import android.app.Application
import com.oliviermarteaux.a055_rebonnte.RebonnteApplication
import com.oliviermarteaux.a055_rebonnte.data.repository.AisleRepository
import com.oliviermarteaux.shared.firebase.firestore.data.repository.PostRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AisleModule {

    @Provides
    @Singleton
    fun provideAisleRepository(
        application: Application
    ): AisleRepository {
        val app = application as RebonnteApplication
        return app.rebonnteContainer.aisleRepository
    }
}