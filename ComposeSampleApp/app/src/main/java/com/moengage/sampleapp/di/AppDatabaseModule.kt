package com.moengage.sampleapp.di

import android.app.Application
import androidx.room.Room
import com.moengage.sampleapp.data.db.AppDatabase
import com.moengage.sampleapp.data.db.ArticleDao
import com.moengage.sampleapp.data.db.DB_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppDatabaseModule {

    @Provides
    @Singleton
    fun provideAppAppDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            DB_NAME,
        ).build()
    }

    @Provides
    @Singleton
    fun provideArticleDao(appDatabase: AppDatabase): ArticleDao {
        return appDatabase.articleDao()
    }
}