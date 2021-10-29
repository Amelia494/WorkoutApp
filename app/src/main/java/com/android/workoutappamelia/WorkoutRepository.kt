package com.android.workoutappamelia

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.android.workoutappamelia.Database.WorkoutDatabase
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "workout-database"

class WorkoutRepository private constructor(context: Context) {

    private val database : WorkoutDatabase = Room.databaseBuilder(
        context.applicationContext,
        WorkoutDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val workoutDao = database.workoutDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun getWorkouts(): LiveData<List<Workout>> = workoutDao.getWorkouts()

    fun getWorkout(id: UUID): LiveData<Workout?> = workoutDao.getWorkout(id)


    fun updateWorkout(workout: Workout) {
        executor.execute {
            workoutDao.updateWorkout(workout)
        }
    }

    fun addWorkout(workout: Workout) {
        executor.execute {
            workoutDao.addWorkout(workout)
        }
    }

    companion object{
        private var INSTANCE: WorkoutRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null){
                INSTANCE = WorkoutRepository(context)
            }
        }

        fun get(): WorkoutRepository {
            return INSTANCE ?:
            throw IllegalStateException("WorkoutRepository must be initialised")
        }
    }
}