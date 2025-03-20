package com.example.androidfinal.base

import android.app.Application
import android.content.Context
import com.cloudinary.android.MediaManager
import com.cloudinary.android.policy.GlobalUploadPolicy
import com.cloudinary.android.policy.UploadPolicy

class MyApplication: Application() {

    object Globals {
        var appContext: Context? = null
    }

    override fun onCreate() {
        super.onCreate()

        val config = mapOf(
            "cloud_name" to "djoau4sur",
            "api_key" to "648643419812657",
            "api_secret" to "OQOPXP0ViuM20qBb6UH7UYLDVxE"
        )

        MediaManager.init(this, config)

        MediaManager.get().globalUploadPolicy = GlobalUploadPolicy.Builder()
            .maxConcurrentRequests(3)
            .networkPolicy(UploadPolicy.NetworkType.UNMETERED)
            .build()

        Globals.appContext = applicationContext
    }
}