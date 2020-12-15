package com.stepsha.di

import android.app.Application
import com.stepsha.api.ApiService
import com.stepsha.db.CommentsDao
import com.stepsha.repo.Repo

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val app: Application) {

    @Provides
    @Singleton
    fun provideAppContext(): Application {
        return app
    }

    @Provides
    @Singleton
    fun provideRepo(apiService: ApiService, commentsDao: CommentsDao) =
        Repo(apiService, commentsDao)

}