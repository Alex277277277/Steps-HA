package com.stepsha.di

import android.app.Application
import com.stepsha.repo.Repo
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        NetworkModule::class,
        DbModule::class
    ]
)
interface AppGraph {
    fun getAppContext(): Application
    fun getRepo(): Repo
}