package com.serranoie.android.cookoutines

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CookoutinesApplication : Application() {

    companion object {
        var instance: CookoutinesApplication? = null
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}