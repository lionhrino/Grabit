package com.example.grabt.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_clients")
data class Client(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nome: String,
    val email: String,
    val password: String,
    val rua: String,
    val codigoPostal: String,
    val concelho: String,
    val distrito: String,
    // ADICIONADO PARA O SISTEMA DE CARTEIRA VIRTUAL DO GRAB!T
    val saldo: Double = 0.0
)