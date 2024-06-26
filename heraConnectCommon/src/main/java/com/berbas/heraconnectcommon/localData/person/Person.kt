package com.berbas.heraconnectcommon.localData.person

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class for a person
 * each variable is a field in the database
 */
@Entity(tableName = "Person")
data class Person(
    var firstname: String,
    var lastname: String,
    var birthday: String,
    var gender: String,
    var height: Int,
    var weight: Double,
    var stepGoal: Int,
    var activityGoal: Double,
    @PrimaryKey
    val id: Int = 1
)
