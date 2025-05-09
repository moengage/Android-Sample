package com.moengage.sampleapp.di

import com.moengage.sampleapp.data.repository.DataStorePreferenceImpl
import com.moengage.sampleapp.data.repository.LocalRepositoryImpl
import com.moengage.sampleapp.data.repository.RemoteRepositoryImpl
import com.moengage.sampleapp.domain.DataStorePreference
import com.moengage.sampleapp.domain.LocalRepository
import com.moengage.sampleapp.domain.RemoteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun provideLocalRepository(localRepositoryImpl: LocalRepositoryImpl): LocalRepository

    @Binds
    @Singleton
    abstract fun provideRemoteRepository(remoteRepositoryImpl: RemoteRepositoryImpl): RemoteRepository

    @Binds
    @Singleton
    abstract fun provideDataStorePreference(dataStorePreferenceImpl: DataStorePreferenceImpl): DataStorePreference
}