package com.example.grabt.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_order_items")
data class OrderItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val pedidoId: Int, // Liga este item à encomenda (Order) acima
    val produtoId: Int,
    val nomeProduto: String, // Guardamos o nome caso o restaurante apague o prato no futuro!
    val quantidade: Int,
    val precoNaHora: Double
)