package com.example.anothertryofmusicanim

import android.app.Application
import timber.log.Timber

class GrandTheftRadioApplication : Application()
{
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}