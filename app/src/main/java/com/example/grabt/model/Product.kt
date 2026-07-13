package com.example.grabt.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_products")
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val restauranteId: Int,
    val nome: String,
    val descricao: String,
    val preco: Double,
    val stock: Int = 0 // NOVO CAMPO: Começa sempre a Zeros quando crias um prato!
)