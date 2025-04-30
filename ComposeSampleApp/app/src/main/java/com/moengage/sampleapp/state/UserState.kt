package com.moengage.sampleapp.state

sealed class UserState {

    class Loading() : UserState()

    class Anonymous() : UserState()

    class LoggedIn() : UserState()

    class LoginFailed() : UserState()
}