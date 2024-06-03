package com.berbas.heraconnectcommon.localData

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

/**
 * Data class for a person
 * each variable is a field in the database
 */
@Entity
data class Person(
    var firstname: String,
    var lastname: String,
    var birthday: String,
    var gender: String,
    var height: String,
    var weight: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)
