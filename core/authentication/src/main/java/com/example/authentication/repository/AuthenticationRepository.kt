package com.example.authentication.repository

import android.net.Uri
import com.example.authentication.service.LogService
import com.example.common.utils.RepositoryResult
import com.example.common.utils.ResponseStatus
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

/**
 * Created by jrzeznicki on 07/02/2023.
 */
class AuthenticationRepository(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseStorage: FirebaseStorage,
    private val logService: LogService
) : AuthenticationDataSource {

    override val currentUserId: String
        get() = firebaseAuth.currentUser?.uid.orEmpty()

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override val hasUser: Boolean
        get() = firebaseAuth.currentUser != null

    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): RepositoryResult<FirebaseUser> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            RepositoryResult.Success(result.user)
        } catch (e: Exception) {
            logService.logNonFatalCrash(e)
            RepositoryResult.Error(
                ResponseStatus().apply {
                    statusMessage = e.message
                    internalStatus = e.hashCode()
                }
            )
        }
    }

    override suspend fun signUpWithEmailAndPassword(
        email: String,
        password: String
    ): RepositoryResult<FirebaseUser> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            RepositoryResult.Success(result.user)
        } catch (e: Exception) {
            logService.logNonFatalCrash(e)
            RepositoryResult.Error(
                ResponseStatus().apply {
                    statusMessage = e.message
                    internalStatus = e.hashCode()
                }
            )
        }
    }

    override suspend fun sendRecoveryEmail(email: String): RepositoryResult<Boolean> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            RepositoryResult.Success(true)
        } catch (e: Exception) {
            logService.logNonFatalCrash(e)
            RepositoryResult.Error(
                ResponseStatus().apply {
                    statusMessage = e.message
                    internalStatus = e.hashCode()
                }
            )
        }
    }

    override suspend fun createAnonymousAccount(): RepositoryResult<FirebaseUser> {
        return try {
            val result = firebaseAuth.signInAnonymously().await()
            RepositoryResult.Success(result.user)
        } catch (e: Exception) {
            logService.logNonFatalCrash(e)
            RepositoryResult.Error(
                ResponseStatus().apply {
                    statusMessage = e.message
                    internalStatus = e.hashCode()
                }
            )
        }
    }

    override suspend fun linkAccount(
        email: String,
        password: String
    ): RepositoryResult<FirebaseUser> {
        return try {
            val credential = EmailAuthProvider.getCredential(email, password)
            val result = firebaseAuth.currentUser!!.linkWithCredential(credential).await()
            RepositoryResult.Success(result.user)
        } catch (e: Exception) {
            logService.logNonFatalCrash(e)
            RepositoryResult.Error(
                ResponseStatus().apply {
                    statusMessage = e.message
                    internalStatus = e.hashCode()
                }
            )
        }
    }

    override suspend fun deleteAccount(): RepositoryResult<Boolean> {
        return try {
            firebaseAuth.currentUser?.delete()?.await()
            RepositoryResult.Success(true)
        } catch (e: Exception) {
            logService.logNonFatalCrash(e)
            RepositoryResult.Error(
                ResponseStatus().apply {
                    statusMessage = e.message
                    internalStatus = e.hashCode()
                }
            )
        }
    }

    override suspend fun signOut(): RepositoryResult<Boolean> {
        return try {
            if (firebaseAuth.currentUser?.isAnonymous == true) {
                firebaseAuth.currentUser?.delete()?.await()
            }
            firebaseAuth.signOut()
            RepositoryResult.Success(true)
        } catch (e: Exception) {
            logService.logNonFatalCrash(e)
            RepositoryResult.Error(
                ResponseStatus().apply {
                    statusMessage = e.message
                    internalStatus = e.hashCode()
                }
            )
        }
    }

    override suspend fun updateName(name: String): RepositoryResult<Boolean> {
        return try {
            val request = UserProfileChangeRequest.Builder().setDisplayName(name).build()
            firebaseAuth.currentUser?.updateProfile(request)?.await()
            RepositoryResult.Success(true)
        } catch (e: Exception) {
            logService.logNonFatalCrash(e)
            RepositoryResult.Error(
                ResponseStatus().apply {
                    statusMessage = e.message
                    internalStatus = e.hashCode()
                }
            )
        }
    }

    override suspend fun uploadPhoto(photoUri: Uri?): RepositoryResult<Uri> {
        return try {
            val uploadTask = firebaseStorage.reference
                .child("$STORAGE_AVATARS_PATH${photoUri?.lastPathSegment.orEmpty()}")
                .putFile(photoUri ?: Uri.EMPTY)
                .await()
            val downloadUri = uploadTask.metadata?.reference?.downloadUrl?.await()
            RepositoryResult.Success(downloadUri)
        } catch (e: Exception) {
            logService.logNonFatalCrash(e)
            RepositoryResult.Error(
                ResponseStatus().apply {
                    statusMessage = e.message
                    internalStatus = e.hashCode()
                }
            )
        }
    }

    override suspend fun updateEmail(email: String): RepositoryResult<Boolean> {
        return try {
            firebaseAuth.currentUser?.updateEmail(email)?.await()
            RepositoryResult.Success(true)
        } catch (e: Exception) {
            logService.logNonFatalCrash(e)
            RepositoryResult.Error(
                ResponseStatus().apply {
                    statusMessage = e.message
                    internalStatus = e.hashCode()
                }
            )
        }
    }

    override suspend fun updatePassword(password: String): RepositoryResult<Boolean> {
        return try {
            firebaseAuth.currentUser?.updatePassword(password)?.await()
            RepositoryResult.Success(true)
        } catch (e: Exception) {
            logService.logNonFatalCrash(e)
            RepositoryResult.Error(
                ResponseStatus().apply {
                    statusMessage = e.message
                    internalStatus = e.hashCode()
                }
            )
        }
    }

    private companion object {
        const val STORAGE_AVATARS_PATH = "avatars/"
    }
}