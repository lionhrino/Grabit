package com.example.grabt.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_orders")
data class Order(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val clienteId: Int,
    val restauranteId: Int,
    val estafetaId: Int? = null, // Pode ser nulo no início porque o restaurante ainda tem de aceitar
    val valorTotal: Double,
    val estado: String = "Pendente", // Pode ser: Pendente, A Preparar, A Aguardar Estafeta, Em Caminho, Entregue
    val dataHora: Long = System.currentTimeMillis() // Guarda o timestamp do momento exato
)