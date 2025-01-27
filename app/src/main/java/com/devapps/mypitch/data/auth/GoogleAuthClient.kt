package com.devapps.mypitch.data.auth

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.devapps.mypitch.R
import com.devapps.mypitch.data.model.SignInResult
import com.devapps.mypitch.data.model.UserData
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await

class GoogleAuthClient(
    private val context: Context,
    private val oneTapClient: SignInClient
) {

    private val authClient = Firebase.auth

    suspend fun signIn() : IntentSender? {
        return try {
            val result = oneTapClient.beginSignIn(buildSignInRequest()).await()
            result?.pendingIntent?.intentSender
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            null
        }
    }

    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(context.getString(R.string.web_client))
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }

    suspend fun signInWithIntent(intent: Intent) : SignInResult {

        val credentials = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credentials.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)

        return try {
            val user = authClient.signInWithCredential(googleCredentials).await().user
            SignInResult(
                data = user?.run {
                    UserData(
                        userId = uid,
                        username = displayName,
                        email = email,
                        userProfileUrl = photoUrl?.toString()
                    )
                },
                errorMessage = null
            )
        } catch (e: Exception) {
            e.printStackTrace()
            if(e is CancellationException) throw e
            SignInResult(
                data = null,
                errorMessage = e.message
            )
        }
    }

    fun getSignedInUser(): UserData? = authClient.currentUser?.run {
        UserData(
            userId = uid,
            username = displayName,
            email = email,
            userProfileUrl = photoUrl?.toString()
        )
    }

    suspend fun signOut() {
        try {
            oneTapClient.signOut().await()
            authClient.signOut()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
        }
    }

}