package com.oliviermarteaux.a055_rebonnte

import android.app.Application
import android.content.Context
import android.util.Log
import coil3.ImageLoader
import coil3.SingletonImageLoader
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.auth.FirebaseAuth
import com.oliviermarteaux.a055_Rebonnte.di.RebonnteAppContainer
import com.oliviermarteaux.a055_Rebonnte.di.RebonnteContainer
import com.oliviermarteaux.shared.firebase.messaging.subscribeToFcmNotificationTopic
import dagger.hilt.android.HiltAndroidApp

/**
 * The application class for the Rebonnte application.
 * This class serves as the entry point for the application and can be used for global application-level
 * initialization tasks such as dependency injection setup using Hilt.
 */
@HiltAndroidApp
class RebonnteApplication : Application(), SingletonImageLoader.Factory {

    lateinit var RebonnteContainer: RebonnteContainer
        internal set

    /**
     * Creates a new [ImageLoader] for the application.
     *
     * @param context The application context.
     * @return A new [ImageLoader] instance.
     */
    override fun newImageLoader(context: Context): ImageLoader {
        return ImageLoader.Builder(context = context)
            .build()
    }

    /**
     * Called when the application is starting, before any activity, service, or receiver objects (excluding content providers) have been created.
     */
    override fun onCreate() {
        super.onCreate()

        RebonnteContainer = createContainer()

        try {
            //_ firebase init
            FirebaseApp.initializeApp(this)
            //_ firebase app check init
            FirebaseAppCheck.getInstance().installAppCheckProviderFactory(
                PlayIntegrityAppCheckProviderFactory.getInstance()
            )
            //_ Firebase authentification: sign out user at app start
            FirebaseAuth.getInstance().signOut()
            val firebaseUser = FirebaseAuth.getInstance().currentUser
            Log.d("OM_TAG", "RebonnteApplication: onCreate(): FirebaseAuth signed out")
            Log.i("OM_TAG", "RebonnteApplication: onCreate(): firebaseUser = $firebaseUser")

//            //_ Firebase cloud messaging: create notif channel and subscribe topic
//            //_ not needed if only one default channel as it is created by MyFirebaseMessaging class
////            createDeviceNotificationChannel(
////                notifManager = getSystemService(NotificationManager::class.java)
////            )
//            //_ Firebase cloud messaging: subscribe to Firebase topic (Mandatory to receive notifs)
            subscribeToFcmNotificationTopic()

            // manage application exceptions
        } catch (e: Exception) {
            Log.e("OM_TAG", "RebonnteApplication: onCreate(): FirebaseApp initialization failed", e)
        }
    }

    private fun createContainer(): RebonnteContainer {
        return try {
            // 👇 class exists ONLY in androidTest
            val androidTestContainerClass = Class.forName(
                "com.oliviermarteaux.a055_Rebonnte.di.RebonnteTestContainer"
            )

            val androidTestContainerConstructor =
                androidTestContainerClass.getConstructor(Context::class.java)
            Log.d("OM_TAG", "RebonnteApplication::createContainer: 🧪 Test container loaded via reflection")
            androidTestContainerConstructor.newInstance(this) as RebonnteContainer

        } catch (e: ClassNotFoundException) {
            Log.d("OM_TAG", "RebonnteApplication::createContainer: 🚀 Prod container loaded")
            RebonnteAppContainer(this)
        }
    }
}