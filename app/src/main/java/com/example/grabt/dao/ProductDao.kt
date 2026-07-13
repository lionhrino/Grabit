package com.example.grabt.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.grabt.model.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM table_products WHERE restauranteId = :restauranteId")
    fun getProductsByRestaurant(restauranteId: Int): Flow<List<Product>>

    @Insert
    fun insert(product: Product)

    @Delete
    fun delete(product: Product)

    @Query("SELECT * FROM table_products")
    fun getAllProducts(): Flow<List<Product>>

    @Query("SELECT COUNT(*) FROM table_products")
    fun getProductCount(): Int

    // NOVO: Função para aumentar ou diminuir o stock!
    @Query("UPDATE table_products SET stock = :novoStock WHERE id = :produtoId")
    fun updateStock(produtoId: Int, novoStock: Int)
}