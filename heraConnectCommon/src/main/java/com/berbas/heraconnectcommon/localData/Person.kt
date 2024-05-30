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
    val firstname: String,
    val lastname: String,
    val birthday: String,
    val gender: String,
    val height: String,
    val weight: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)
