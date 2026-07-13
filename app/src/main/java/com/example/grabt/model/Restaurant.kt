package com.example.grabt.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_restaurants")
data class Restaurant(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // O autonumber obrigatório!
    val nome: String,
    val email: String,
    val password: String,
    val rua: String,
    val codigoPostal: String,
    val concelho: String,
    val distrito: String,
    val saldo: Double = 0.0 // Saldo arranca a zeros
)