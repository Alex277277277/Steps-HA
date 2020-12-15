package com.stepsha

import android.app.Application
import com.stepsha.di.AppGraph
import com.stepsha.di.AppModule
import com.stepsha.di.DaggerAppGraph

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        appGraph = DaggerAppGraph.builder()
            .appModule(AppModule(this))
            .build()
    }

    companion object {
        lateinit var appGraph: AppGraph
    }

}