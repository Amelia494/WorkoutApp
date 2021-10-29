package com.android.workoutappamelia.Database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.android.workoutappamelia.Workout
import java.util.*

@Dao
interface WorkoutDao{

    @Query("SELECT * FROM workout")
    fun getWorkouts(): LiveData<List<Workout>>

    @Query("SELECT * FROM workout WHERE id=(:id)")
    fun getWorkout(id: UUID): LiveData<Workout?>

    @Update
    fun updateWorkout(workout: Workout)

    @Insert
    fun addWorkout(workout: Workout)

}