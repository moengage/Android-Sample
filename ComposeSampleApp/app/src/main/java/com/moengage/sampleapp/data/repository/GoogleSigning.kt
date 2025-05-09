package com.moengage.sampleapp.data.repository

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.moengage.sampleapp.logger.LogLevel
import com.moengage.sampleapp.logger.log
import com.moengage.sampleapp.util.BASE_TAG
import com.moengage.sampleapp.util.GOOGLE_SIGNING_WEB_CLIENT_ID
import java.security.MessageDigest
import java.util.UUID

class GoogleSigning(private val context: Context) {

    private val tag = "${BASE_TAG}_GoogleSigning"

    suspend fun login(onLogin: (result: GetCredentialResponse?) -> Unit) {
        try {
            log(tag) { "login(): " }
            val credentialManager = CredentialManager.create(context)
            val result = credentialManager.getCredential(
                request = GetCredentialRequest.Builder()
                    .addCredentialOption(getSignInWithGoogleOption())
                    .build(),
                context = context,
            )
            onLogin(result)
        } catch (t: Throwable) {
            log(tag, LogLevel.ERROR, t) { "login(): " }
            onLogin(null)
        }
    }

    suspend fun logout(onLogout: () -> Unit) {
        try {
            log(tag) { "logout(): Started" }
            CredentialManager.create(context).clearCredentialState(ClearCredentialStateRequest())
            onLogout()
            log(tag) { "logout(): Completed" }
        } catch (t: Throwable) {
            log(tag, LogLevel.ERROR, t) { "logout(): " }
        }
    }

    private fun getSignInWithGoogleOption(): GetSignInWithGoogleOption {
        log(tag) { "getSignInWithGoogleOption(): " }
        return GetSignInWithGoogleOption.Builder(GOOGLE_SIGNING_WEB_CLIENT_ID)
            .setNonce(
                MessageDigest.getInstance("SHA-256")
                    .digest(UUID.randomUUID().toString().toByteArray()).fold("") { str, it ->
                        str + "%02x".format(it)
                    }
            )
            .build()
    }
}