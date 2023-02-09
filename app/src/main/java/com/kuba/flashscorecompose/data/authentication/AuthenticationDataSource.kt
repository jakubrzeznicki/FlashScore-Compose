package com.kuba.flashscorecompose.data.authentication

import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import com.kuba.flashscorecompose.utils.RepositoryResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

/**
 * Created by jrzeznicki on 07/02/2023.
 */
interface AuthenticationDataSource {
    val currentUserId: String
    val currentUser: FirebaseUser?
    val hasUser: Boolean
    suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): RepositoryResult<FirebaseUser>

    suspend fun signUpWithEmailAndPassword(
        email: String,
        password: String
    ): RepositoryResult<FirebaseUser>

    suspend fun sendRecoveryEmail(email: String): RepositoryResult<Boolean>
    suspend fun createAnonymousAccount(): RepositoryResult<FirebaseUser>
    suspend fun linkAccount(email: String, password: String): RepositoryResult<FirebaseUser>
    suspend fun deleteAccount(): RepositoryResult<Boolean>
    suspend fun signOut(): RepositoryResult<Boolean>
    suspend fun updateName(name: String): RepositoryResult<Boolean>
    suspend fun updatePhotoUrl(photoUri: Uri?): RepositoryResult<Boolean>
    suspend fun updateEmail(email: String): RepositoryResult<Boolean>
    suspend fun updatePassword(password: String): RepositoryResult<Boolean>
    fun getAuthState(viewModelScope: CoroutineScope): StateFlow<Boolean>
}