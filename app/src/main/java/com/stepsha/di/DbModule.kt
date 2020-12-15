package com.stepsha.di

import android.app.Application
import androidx.room.Room
import com.stepsha.db.AppDb
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DbModule {

    @Provides
    @Singleton
    fun provideAppDb(appContext: Application): AppDb =
        Room.databaseBuilder(appContext, AppDb::class.java, DB_NAME)
            .build()

    @Provides
    @Singleton
    fun provideCommentsDao(db: AppDb) = db.commentsDao()

    companion object {
        const val DB_NAME = "db_steps_ha"
    }

}