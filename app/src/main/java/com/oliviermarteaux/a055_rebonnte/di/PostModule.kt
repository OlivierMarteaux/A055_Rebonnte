package com.oliviermarteaux.a055_rebonnte.di

import android.app.Application
import com.oliviermarteaux.a055_rebonnte.RebonnteApplication
import com.oliviermarteaux.shared.firebase.firestore.data.repository.PostRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * This class acts as a Dagger Hilt module, responsible for providing dependencies to other parts of the application.
 * It's installed in the SingletonComponent, ensuring that dependencies provided by this module are created only once
 * and remain available throughout the application's lifecycle.
 */
@Module
@InstallIn(SingletonComponent::class)
class PostModule {

    @Provides
    @Singleton
    fun providePostRepository(
        application: Application
    ): PostRepository {
        val app = application as RebonnteApplication
        return app.rebonnteContainer.postRepository
    }
}
