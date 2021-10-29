package com.android.workoutappamelia.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.android.workoutappamelia.Workout

@Database(entities = [Workout::class], version=1)
@TypeConverters(WorkoutConverter::class)
abstract class WorkoutDatabase : RoomDatabase() {

    abstract fun workoutDao(): WorkoutDao
}