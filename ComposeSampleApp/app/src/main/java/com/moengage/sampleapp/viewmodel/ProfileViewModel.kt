package com.moengage.sampleapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.moengage.sampleapp.data.repository.Repository
import com.moengage.sampleapp.libaries.moengage.MoEngageHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val application: Application,
    private val repository: Repository
) : AndroidViewModel(application) {

    fun getUserId(): Flow<String?> {
        return repository.getUserId()
    }

    fun updateUserId(userId: String) {
        viewModelScope.launch {
            if (userId.isNotBlank()) {
                repository.saveUserId(userId)
                MoEngageHelper.serIdentifier(application, userId)
            }
        }
    }

    fun getFirstName(): Flow<String?> {
        return repository.getFirstName()
    }

    fun saveFirstName(firstName: String) {
        viewModelScope.launch {
            if (firstName.isNotBlank()) {
                repository.saveFirstName(firstName)
                MoEngageHelper.setFirstName(application, firstName)
            }
        }
    }

    fun getLastName(): Flow<String?> {
        return repository.getLastName()
    }

    fun saveLastName(lastName: String) {
        viewModelScope.launch {
            if (lastName.isNotBlank()) {
                repository.saveLastName(lastName)
                MoEngageHelper.setLastName(application, lastName)
            }
        }
    }

    fun getEmail(): Flow<String?> {
        return repository.getEmail()
    }

    fun saveEmail(email: String) {
        viewModelScope.launch {
            if (email.isNotBlank()) {
                repository.saveEmail(email)
                MoEngageHelper.setEmail(application, email)
            }
        }
    }

    fun getGaidStatus(): Flow<Boolean> {
        return repository.getGaidStatus()
    }

    fun saveGaidStatus(optedIn: Boolean) {
        viewModelScope.launch {
            repository.saveGaidStatus(optedIn)
            MoEngageHelper.changeGaidStatus(application, optedIn)
        }
    }
}