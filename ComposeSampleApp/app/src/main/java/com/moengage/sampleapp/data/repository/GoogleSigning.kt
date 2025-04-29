package com.moengage.sampleapp.data.repository

import android.app.Application
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.moengage.sampleapp.util.GOOGLE_SIGNING_WEB_CLIENT_ID
import jakarta.inject.Inject
import java.security.MessageDigest
import java.util.UUID

class GoogleSigning @Inject constructor(private val application: Application) {

    suspend fun login(onLogin: (result: GetCredentialResponse) -> Unit) {
        val credentialManager = CredentialManager.create(application)
        val result = credentialManager.getCredential(
            request = GetCredentialRequest.Builder()
                .addCredentialOption(getSignInWithGoogleOption())
                .build(),
            context = application,
        )
        onLogin(result)
    }

    private fun getSignInWithGoogleOption(): GetSignInWithGoogleOption {
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