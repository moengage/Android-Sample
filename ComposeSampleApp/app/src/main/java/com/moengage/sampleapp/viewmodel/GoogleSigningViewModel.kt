package com.moengage.sampleapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialResponse
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.moengage.sampleapp.data.repository.GoogleSigning
import com.moengage.sampleapp.data.repository.Repository
import com.moengage.sampleapp.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class GoogleSigningViewModel @Inject constructor(
    private val googleSigning: GoogleSigning,
    private val repository: Repository
) : ViewModel() {

    val loginState = mutableStateOf<UiState?>(null)

    fun login() {
        viewModelScope.launch {
            loginState.value = UiState.Loading()
            googleSigning.login {
                viewModelScope.launch {
                    handleSignIn(it)
                }
            }
        }
    }

    suspend fun handleSignIn(result: GetCredentialResponse) {
        val credential = result.credential
        if (credential is CustomCredential &&
            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            try {
                val googleIdTokenCredential = GoogleIdTokenCredential
                    .createFrom(credential.data)
                val googleIdToken = googleIdTokenCredential.idToken
                val authCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
                val user = Firebase.auth.signInWithCredential(authCredential).await().user
                user?.run {
                    repository.saveUserId(user.uid)
                    user.email?.let { repository.saveEmail(it) }
                    loginState.value = UiState.Success()
                }
            } catch (t: Throwable) {
                loginState.value = UiState.Failure()
            }
        }
    }

}