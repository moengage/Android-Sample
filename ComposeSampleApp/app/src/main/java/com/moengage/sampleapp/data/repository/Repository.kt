package com.moengage.sampleapp.data.repository

import com.moengage.sampleapp.domain.DataStorePreference
import com.moengage.sampleapp.domain.LocalRepository
import com.moengage.sampleapp.domain.RemoteRepository
import javax.inject.Inject

class Repository @Inject constructor(
    localRepository: LocalRepository,
    dataStorePreference: DataStorePreference,
    remoteRepository: RemoteRepository
) : LocalRepository by localRepository,
    DataStorePreference by dataStorePreference,
    RemoteRepository by remoteRepository