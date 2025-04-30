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
import com.moengage.sampleapp.data.repository.Repository
import com.moengage.sampleapp.logger.LogLevel
import com.moengage.sampleapp.logger.log
import com.moengage.sampleapp.state.UserState
import com.moengage.sampleapp.util.BASE_TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class GoogleSigningViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val tag = "${BASE_TAG}_GoogleSigningViewModel"

    val userState = mutableStateOf<UserState>(UserState.Loading())

    init {
        repository.getUserId().onEach { userId ->
            if (userId.isNullOrBlank()) {
                userState.value = UserState.Anonymous()
            } else {
                userState.value = UserState.LoggedIn()
            }
        }.launchIn(viewModelScope)
    }

    fun handleSignIn(result: GetCredentialResponse?) {
        viewModelScope.launch {
            log(tag) { "handleSignIn(): " }
            val credential = result?.credential ?: run {
                log(tag, LogLevel.WARN) { "handleSignIn(): LoggedIn due to result as null" }
                userState.value = UserState.LoginFailed()
                return@launch
            }
            if (credential is CustomCredential &&
                credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
            ) {
                log(tag) { "handleSignIn(): processing custom credentials" }
                try {
                    val googleIdTokenCredential = GoogleIdTokenCredential
                        .createFrom(credential.data)
                    val googleIdToken = googleIdTokenCredential.idToken
                    val authCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
                    val user = Firebase.auth.signInWithCredential(authCredential).await().user
                    log(tag) { "handleSignIn(): processed custom credentials" }
                    user?.run {
                        log(tag) { "handleSignIn(): User resolved with userId = ${user.uid}" }
                        viewModelScope.launch {
                            repository.saveUserId(user.uid)
                            user.email?.let { repository.saveEmail(it) }
                        }
                        userState.value = UserState.LoggedIn()
                    }
                    log(tag) { "handleSignIn(): Login Completed" }
                } catch (t: Throwable) {
                    log(tag, LogLevel.ERROR, t) { "handleSignIn(): " }
                    userState.value = UserState.LoginFailed()
                }
            } else {
                log(tag, LogLevel.WARN) { "handleSignIn(): Credential type not supported" }
                userState.value = UserState.LoginFailed()
            }
        }
    }

}