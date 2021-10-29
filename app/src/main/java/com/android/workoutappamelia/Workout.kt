package com.android.workoutappamelia

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Workout ( @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    var title: String = "",
    var date: Date = Date(),
    var start: Date = Date(),
    var finish: Date = Date(),
    var place: String = "",
    var isIndividual: Boolean = false,
    var isGroup: Boolean = false

)