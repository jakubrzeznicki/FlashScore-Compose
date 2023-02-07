package com.kuba.flashscorecompose.account.service

import com.kuba.flashscorecompose.account.model.User
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 05/02/2023.
 */
interface AccountService {
    val currentUserId: String
    val hasUser: Boolean
    val currentUser: Flow<User>
    suspend fun authenticate(email: String, password: String)
    suspend fun sendRecoveryEmail(email: String)
    suspend fun createAnonymousAccount()
    suspend fun linkAccount(email: String, password: String)
    suspend fun signUpWithEmailAndPassword(email: String, password: String)
    suspend fun deleteAccount()
    suspend fun signOut()
}
