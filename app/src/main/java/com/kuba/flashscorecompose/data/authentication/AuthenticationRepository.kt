package com.kuba.flashscorecompose.data.authentication

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.kuba.flashscorecompose.account.service.LogService
import com.kuba.flashscorecompose.utils.RepositoryResult
import com.kuba.flashscorecompose.utils.ResponseStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.tasks.await

/**
 * Created by jrzeznicki on 07/02/2023.
 */
class AuthenticationRepository(
    private val firebaseAuth: FirebaseAuth,
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
            RepositoryResult.Error(ResponseStatus().apply {
                statusMessage = e.message
                internalStatus = e.hashCode()
            })
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
            RepositoryResult.Error(ResponseStatus().apply {
                statusMessage = e.message
                internalStatus = e.hashCode()
            })
        }
    }

    override suspend fun sendRecoveryEmail(email: String): RepositoryResult<Boolean> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            RepositoryResult.Success(true)
        } catch (e: Exception) {
            logService.logNonFatalCrash(e)
            RepositoryResult.Error(ResponseStatus().apply {
                statusMessage = e.message
                internalStatus = e.hashCode()
            })
        }
    }

    override suspend fun createAnonymousAccount(): RepositoryResult<FirebaseUser> {
        return try {
            val result = firebaseAuth.signInAnonymously().await()
            RepositoryResult.Success(result.user)
        } catch (e: Exception) {
            logService.logNonFatalCrash(e)
            RepositoryResult.Error(ResponseStatus().apply {
                statusMessage = e.message
                internalStatus = e.hashCode()
            })
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
            RepositoryResult.Error(ResponseStatus().apply {
                statusMessage = e.message
                internalStatus = e.hashCode()
            })
        }
    }

    override suspend fun deleteAccount(): RepositoryResult<Boolean> {
        return try {
            firebaseAuth.currentUser!!.delete().await()
            RepositoryResult.Success(true)
        } catch (e: Exception) {
            logService.logNonFatalCrash(e)
            RepositoryResult.Error(ResponseStatus().apply {
                statusMessage = e.message
                internalStatus = e.hashCode()
            })
        }
    }

    override suspend fun signOut(): RepositoryResult<Boolean> {
        return try {
            if (firebaseAuth.currentUser!!.isAnonymous) firebaseAuth.currentUser!!.delete()
            firebaseAuth.signOut()
            RepositoryResult.Success(true)
        } catch (e: Exception) {
            logService.logNonFatalCrash(e)
            RepositoryResult.Error(ResponseStatus().apply {
                statusMessage = e.message
                internalStatus = e.hashCode()
            })
        }
    }

    override fun getAuthState(viewModelScope: CoroutineScope) = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser == null)
        }
        firebaseAuth.addAuthStateListener(authStateListener)
        awaitClose {
            firebaseAuth.removeAuthStateListener(authStateListener)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), firebaseAuth.currentUser == null)
}