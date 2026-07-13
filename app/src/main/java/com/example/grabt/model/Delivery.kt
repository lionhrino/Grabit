package com.example.grabt.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_deliveries")
data class Delivery(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val email: String,
    val password: String,
    val vehicle: String,
    val phone: String,
    val balance: Double = 0.0
)