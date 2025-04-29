package com.moengage.sampleapp.state

sealed class UiState {

    class Loading() : UiState()

    class Success() : UiState()

    class Failure() : UiState()
}