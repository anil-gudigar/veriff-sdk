package com.veriff.sample

import android.app.Application
import com.veriff.sdk.core.app.VeriffApp

class SampleApp: Application() {
    override fun onCreate() {
        super.onCreate()
        VeriffApp.initialize(this)
    }
}