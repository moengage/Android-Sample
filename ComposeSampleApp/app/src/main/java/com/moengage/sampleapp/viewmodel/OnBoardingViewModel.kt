package com.moengage.sampleapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moengage.sampleapp.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    var keepSplashVisible = mutableStateOf(true)
        private set
    var isOnBoardingCompleted = mutableStateOf(false)
        private set

    init {
        repository.getOnBoardingStatus().onEach { status ->
            isOnBoardingCompleted.value = status
            delay(1000)
            keepSplashVisible.value = false
        }.launchIn(viewModelScope)
    }

    fun updateOnBoardingStatus(isOnBoardingCompleted: Boolean) {
        viewModelScope.launch {
            repository.saveOnBoardingStatus(isOnBoardingCompleted)
        }
    }
}