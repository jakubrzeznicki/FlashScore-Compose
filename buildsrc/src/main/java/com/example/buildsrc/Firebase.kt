package com.example.buildsrc

/**
 * Created by jrzeznicki on 13/03/2023.
 */
object Firebase {
    private const val crashlyticsVersion = "18.3.5"
    private const val analyticsVersion = "21.2.0"
    private const val authVersion = "21.1.0"
    private const val perfVersion = "20.3.1"
    private const val storageVersion = "20.1.0"
    const val crashlytics = "com.google.firebase:firebase-crashlytics-ktx:$crashlyticsVersion"
    const val analytics = "com.google.firebase:firebase-analytics-ktx:$analyticsVersion"
    const val auth = "com.google.firebase:firebase-auth-ktx:$authVersion"
    const val perf = "com.google.firebase:firebase-perf-ktx:$perfVersion"
    const val storage = "com.google.firebase:firebase-storage-ktx:$storageVersion"
}