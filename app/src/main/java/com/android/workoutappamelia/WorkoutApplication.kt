package com.android.workoutappamelia

import android.app.Application

class WorkoutApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        WorkoutRepository.initialize(this)
    }
}