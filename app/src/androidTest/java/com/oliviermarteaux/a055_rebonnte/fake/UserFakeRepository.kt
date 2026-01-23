package com.oliviermarteaux.a055_rebonnte.fake

import android.util.Log
import androidx.annotation.StringRes
import com.google.firebase.auth.FirebaseUser
import com.oliviermarteaux.shared.firebase.authentication.data.repository.UserRepository
import com.oliviermarteaux.shared.firebase.authentication.domain.model.NewUser
import com.oliviermarteaux.shared.firebase.authentication.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * Repository for managing user data.
 */
//@Singleton
class UserFakeRepository: UserRepository {

    init {
        Log.d("OM_TAG", "UserFakeRepository: created and injected")
    }

    val testUser = User(
        id = "1",
        firstname = "Fievel",
        lastname = "Farwest",
        fullname = "Fievel Farwest",
        email = "fievelfarwest@example.com",
        photoUrl = ""
    )
    /**
     * A flow that emits the current authentication state of the user.
     * Emits a [FirebaseUser] if a user is signed in, or `null` otherwise.
     */
    override val userAuthState: Flow<User?> = flowOf(testUser)
    /**
     * Checks if an email address is already registered.
     *
     * @param email The email address to check.
     * @return A [Result] indicating whether the email exists. `Result.success(true)` if it exists, `Result.success(false)` otherwise.
     */
    override suspend fun checkEmail(email: String): Result<Boolean> = Result.success(true)
    /**
     * Creates a new user account.
     *
     * @param newUser The details of the new user.
     * @return A [Result] containing the created [User] on success, or an error.
     */
    override suspend fun createAccount(newUser: NewUser): Result<User?> = Result.success(testUser)
    /**
     * Signs in a user with their email and password.
     *
     * @param email The user's email address.
     * @param password The user's password.
     * @return A [Result] containing the signed-in [User] on success, or an error.
     */
    override suspend fun signIn(email: String, password: String): Result<User?> {
        Log.d("OM_TAG", "UserFakeRepository:signIn(): ${testUser.fullname}-${testUser.photoUrl} signed in")
        return Result.success(testUser)
    }
    /**
     * Sends a password reset email to the specified email address.
     *
     * @param email The email address to send the reset link to.
     * @return A [Result] indicating success or failure.
     */
    override suspend fun sendPasswordResetEmail(email: String): Result<Unit> =
        Result.success(Unit)
    /**
     * Signs out the current user.
     *
     * @return A [Result] containing the signed-out [User] on success, or an error.
     */
    override fun signOut(): Result<User?> = Result.success(testUser)
    /**
     * Deletes the current user's account.
     *
     * @return A [Result] containing the deleted [User] on success, or an error.
     */
    override suspend fun deleteAccount(): Result<User?> = Result.success(testUser)


    override suspend fun signInWithGoogle(@StringRes serverClientIdStringRes: Int): Result<User?> =
        Result.success(testUser)
}